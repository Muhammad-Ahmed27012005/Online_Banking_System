// Global variables
let currentTransactionType = 'DEPOSIT';
let accountBalance = 0;

// Initialize on page load
document.addEventListener('DOMContentLoaded', function() {
    // Set initial balance from server
    fetch('/account')
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                accountBalance = data.balance;
                document.getElementById('balanceDisplay').innerHTML = 
                    `$${accountBalance.toFixed(2)}`;
            }
        });
    
    // Set up event listeners
    document.getElementById('amount').addEventListener('input', validateAmount);
    document.getElementById('receiverAccount')?.addEventListener('input', validateAmount);
});

// Select transaction type
function selectTransactionType(type) {
    currentTransactionType = type;
    
    // Update button states
    document.querySelectorAll('.type-option').forEach(btn => {
        btn.classList.remove('active');
        if (btn.dataset.type === type) {
            btn.classList.add('active');
        }
    });
    
    // Show/hide receiver account field
    const receiverGroup = document.getElementById('receiverGroup');
    if (type === 'TRANSFER') {
        receiverGroup.style.display = 'block';
    } else {
        receiverGroup.style.display = 'none';
    }
    
    // Update button text
    const btnText = document.getElementById('btnText');
    switch(type) {
        case 'DEPOSIT':
            btnText.textContent = 'Submit Deposit';
            break;
        case 'WITHDRAW':
            btnText.textContent = 'Submit Withdrawal';
            break;
        case 'TRANSFER':
            btnText.textContent = 'Submit Transfer';
            break;
    }
    
    validateAmount();
}

// Validate input amount
function validateAmount() {
    const amountInput = document.getElementById('amount');
    const amount = parseFloat(amountInput.value);
    const submitBtn = document.getElementById('submitBtn');
    
    let isValid = false;
    
    if (amount && amount > 0) {
        if (currentTransactionType === 'WITHDRAW' && amount > accountBalance) {
            showFeedback('Error: Insufficient balance for withdrawal', 'error');
            isValid = false;
        } else if (currentTransactionType === 'TRANSFER') {
            const receiverAccount = document.getElementById('receiverAccount').value;
            if (receiverAccount && receiverAccount.length >= 4) {
                if (amount > accountBalance) {
                    showFeedback('Error: Insufficient balance for transfer', 'error');
                    isValid = false;
                } else {
                    isValid = true;
                }
            } else {
                isValid = false;
            }
        } else {
            isValid = true;
        }
    }
    
    submitBtn.disabled = !isValid;
    return isValid;
}

// Process transaction
function processTransaction() {
    if (!validateAmount()) return;
    
    const amount = parseFloat(document.getElementById('amount').value);
    const submitBtn = document.getElementById('submitBtn');
    const btnText = document.getElementById('btnText');
    
    // Show loading state
    submitBtn.disabled = true;
    btnText.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Processing...';
    
    let apiUrl = '';
    let requestData = {};
    
    switch(currentTransactionType) {
        case 'DEPOSIT':
            apiUrl = '/deposit';
            requestData = { amount: amount };
            break;
        case 'WITHDRAW':
            apiUrl = '/withdraw';
            requestData = { amount: amount };
            break;
        case 'TRANSFER':
            const receiverAccount = document.getElementById('receiverAccount').value;
            apiUrl = '/transfer';
            requestData = { 
                amount: amount,
                toAccount: parseInt(receiverAccount)
            };
            break;
    }
    
    // Send request to backend
    fetch(apiUrl, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: new URLSearchParams(requestData)
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            // Update balance
            accountBalance = data.newBalance;
            document.getElementById('balanceDisplay').innerHTML = 
                `$${accountBalance.toFixed(2)}`;
            
            // Show success message
            showFeedback(data.message, 'success');
            
            // Add to transaction history
            addTransactionToHistory(data.transaction);
            
            // Reset form
            resetForm();
        } else {
            // Show error message
            showFeedback(data.message, 'error');
        }
    })
    .catch(error => {
        showFeedback('Error: Unable to process transaction', 'error');
        console.error('Error:', error);
    })
    .finally(() => {
        // Reset button state
        submitBtn.disabled = false;
        btnText.innerHTML = '<i class="fas fa-paper-plane"></i> ' + 
            (currentTransactionType === 'DEPOSIT' ? 'Submit Deposit' :
             currentTransactionType === 'WITHDRAW' ? 'Submit Withdrawal' : 'Submit Transfer');
    });
}

// Show feedback message
function showFeedback(message, type) {
    const feedbackDiv = document.getElementById('feedbackMessage');
    feedbackDiv.innerHTML = `<p>${message}</p>`;
    feedbackDiv.className = 'feedback-message ' + type;
    
    // Auto-clear success messages after 5 seconds
    if (type === 'success') {
        setTimeout(() => {
            feedbackDiv.innerHTML = '<p>Select a transaction type and enter amount to begin</p>';
            feedbackDiv.className = 'feedback-message';
        }, 5000);
    }
}

// Add transaction to history display
function addTransactionToHistory(transaction) {
    const historyDiv = document.getElementById('transactionHistory');
    const noTransactions = historyDiv.querySelector('.no-transactions');
    
    if (noTransactions) {
        noTransactions.remove();
    }
    
    const transactionDiv = document.createElement('div');
    transactionDiv.className = `transaction-item ${transaction.type.toLowerCase()}`;
    
    const typeIcon = transaction.type === 'DEPOSIT' ? 'fa-arrow-down' :
                    transaction.type === 'WITHDRAWAL' ? 'fa-arrow-up' : 'fa-exchange-alt';
    const typeColor = transaction.type === 'DEPOSIT' ? '#4CAF50' :
                     transaction.type === 'WITHDRAWAL' ? '#f44336' : '#2196F3';
    
    const date = new Date().toLocaleTimeString();
    
    transactionDiv.innerHTML = `
        <div style="display: flex; justify-content: space-between; align-items: center;">
            <div>
                <i class="fas ${typeIcon}" style="color: ${typeColor}; margin-right: 8px;"></i>
                <strong>${transaction.type}</strong>
            </div>
            <span style="font-weight: bold; color: ${typeColor};">$${transaction.amount.toFixed(2)}</span>
        </div>
        <div style="font-size: 0.85rem; color: #666; margin-top: 5px;">
            ${transaction.description} • ${date}
        </div>
    `;
    
    // Add to top of history
    historyDiv.insertBefore(transactionDiv, historyDiv.firstChild);
    
    // Limit history to 5 items
    const items = historyDiv.querySelectorAll('.transaction-item');
    if (items.length > 5) {
        items[5].remove();
    }
}

// Reset form after successful transaction
function resetForm() {
    document.getElementById('amount').value = '';
    document.getElementById('receiverAccount').value = '';
    validateAmount();
}

// Refresh balance from server
function refreshBalance() {
    const refreshBtn = document.querySelector('.refresh-btn');
    const originalText = refreshBtn.innerHTML;
    
    refreshBtn.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Refreshing...';
    refreshBtn.disabled = true;
    
    fetch('/account')
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                accountBalance = data.balance;
                document.getElementById('balanceDisplay').innerHTML = 
                    `$${accountBalance.toFixed(2)}`;
                showFeedback('Balance updated successfully', 'success');
            }
        })
        .catch(error => {
            showFeedback('Error refreshing balance', 'error');
        })
        .finally(() => {
            refreshBtn.innerHTML = originalText;
            refreshBtn.disabled = false;
        });
}