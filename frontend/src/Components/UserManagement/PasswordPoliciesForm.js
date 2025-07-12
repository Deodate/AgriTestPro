import React, { useState } from 'react';
import './PasswordPoliciesForm.css';

const PasswordPoliciesForm = () => {
    const [minimumLength, setMinimumLength] = useState('');
    const [requiresUppercase, setRequiresUppercase] = useState(false);
    const [requiresLowercase, setRequiresLowercase] = useState(false);
    const [requiresNumber, setRequiresNumber] = useState(false);
    const [requiresSpecialCharacter, setRequiresSpecialCharacter] = useState(false);
    const [historySize, setHistorySize] = useState('');
    const [expirationPeriod, setExpirationPeriod] = useState('');

    const handleSubmit = (e) => {
        e.preventDefault();
        // Handle form submission logic here
        console.log({
            minimumLength,
            requiresUppercase,
            requiresLowercase,
            requiresNumber,
            requiresSpecialCharacter,
            historySize,
            expirationPeriod
        });
        // Clear form or show success message
    };

    const handleCancel = () => {
        // Handle cancel logic here, maybe close the form or clear fields
        console.log('Form cancelled');
    };

    return (
        <div className="password-policies-form-container">
            <form className="password-policies-form" onSubmit={handleSubmit}>
                <div className="form-row">
                    <input
                        type="number"
                        placeholder="Minimum Length"
                        value={minimumLength}
                        onChange={(e) => setMinimumLength(e.target.value)}
                        required
                    />
                </div>
                <div className="form-row checkbox-row">
                    <input
                        type="checkbox"
                        id="requiresUppercase"
                        checked={requiresUppercase}
                        onChange={(e) => setRequiresUppercase(e.target.checked)}
                    />
                    <label htmlFor="requiresUppercase">Requires Uppercase</label>
                </div>
                <div className="form-row checkbox-row">
                    <input
                        type="checkbox"
                        id="requiresLowercase"
                        checked={requiresLowercase}
                        onChange={(e) => setRequiresLowercase(e.target.checked)}
                    />
                    <label htmlFor="requiresLowercase">Requires Lowercase</label>
                </div>
                <div className="form-row checkbox-row">
                    <input
                        type="checkbox"
                        id="requiresNumber"
                        checked={requiresNumber}
                        onChange={(e) => setRequiresNumber(e.target.checked)}
                    />
                    <label htmlFor="requiresNumber">Requires Number</label>
                </div>
                <div className="form-row checkbox-row">
                    <input
                        type="checkbox"
                        id="requiresSpecialCharacter"
                        checked={requiresSpecialCharacter}
                        onChange={(e) => setRequiresSpecialCharacter(e.target.checked)}
                    />
                    <label htmlFor="requiresSpecialCharacter">Requires Special Character</label>
                </div>
                 <div className="form-row">
                     <input
                        type="number"
                        placeholder="History Size"
                        value={historySize}
                        onChange={(e) => setHistorySize(e.target.value)}
                        required
                    />
                </div>
                 <div className="form-row">
                     <input
                        type="number"
                        placeholder="Expiration Period (days)"
                        value={expirationPeriod}
                        onChange={(e) => setExpirationPeriod(e.target.value)}
                        required
                    />
                </div>

                <div className="button-row">
                    <button type="submit">Submit</button>
                    <button type="button" onClick={handleCancel}>Cancel</button>
                </div>
            </form>
        </div>
    );
};

export default PasswordPoliciesForm; 