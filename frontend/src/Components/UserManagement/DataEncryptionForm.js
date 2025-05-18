import React, { useState } from 'react';
import './DataEncryptionForm.css';

const DataEncryptionForm = () => {
    const [encryptionKeyId, setEncryptionKeyId] = useState('');
    const [algorithmUsed, setAlgorithmUsed] = useState('');
    const [keyCreationDate, setKeyCreationDate] = useState('');
    const [keyExpirationDate, setKeyExpirationDate] = useState('');
    const [associatedDataType, setAssociatedDataType] = useState('');

    const handleSubmit = (e) => {
        e.preventDefault();
        // Handle form submission logic here
        console.log({
            encryptionKeyId,
            algorithmUsed,
            keyCreationDate,
            keyExpirationDate,
            associatedDataType
        });
        // Clear form or show success message
    };

    const handleCancel = () => {
        // Handle cancel logic here, maybe close the form or clear fields
        console.log('Form cancelled');
    };

    return (
        <div className="data-encryption-form-container">
            <form className="data-encryption-form" onSubmit={handleSubmit}>
                <div className="form-row">
                    <input
                        type="text"
                        placeholder="Encryption Key ID"
                        value={encryptionKeyId}
                        onChange={(e) => setEncryptionKeyId(e.target.value)}
                        required
                    />
                </div>
                <div className="form-row">
                    <input
                        type="text"
                        placeholder="Algorithm Used"
                        value={algorithmUsed}
                        onChange={(e) => setAlgorithmUsed(e.target.value)}
                        required
                    />
                </div>
                 <div className="form-row">
                     <input
                        type="date"
                        placeholder="Key Creation Date"
                        value={keyCreationDate}
                        onChange={(e) => setKeyCreationDate(e.target.value)}
                        required
                    />
                </div>
                 <div className="form-row">
                     <input
                        type="date"
                        placeholder="Key Expiration Date"
                        value={keyExpirationDate}
                        onChange={(e) => setKeyExpirationDate(e.target.value)}
                        required
                    />
                </div>
                 <div className="form-row">
                    <input
                        type="text"
                        placeholder="Associated Data Type"
                        value={associatedDataType}
                        onChange={(e) => setAssociatedDataType(e.target.value)}
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

export default DataEncryptionForm; 