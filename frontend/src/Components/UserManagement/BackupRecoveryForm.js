import React, { useState } from 'react';
import './BackupRecoveryForm.css';

const BackupRecoveryForm = () => {
    const [backupSchedule, setBackupSchedule] = useState('');
    const [lastBackupDate, setLastBackupDate] = useState('');
    const [recoveryPointObjective, setRecoveryPointObjective] = useState('');
    const [recoveryTimeObjective, setRecoveryTimeObjective] = useState('');
    const [backupLocation, setBackupLocation] = useState('');

    const handleSubmit = (e) => {
        e.preventDefault();
        // Handle form submission logic here
        console.log({
            backupSchedule,
            lastBackupDate,
            recoveryPointObjective,
            recoveryTimeObjective,
            backupLocation
        });
        // Clear form or show success message
    };

    const handleCancel = () => {
        // Handle cancel logic here, maybe close the form or clear fields
        console.log('Form cancelled');
    };

    return (
        <div className="backup-recovery-form-container">
            <form className="backup-recovery-form" onSubmit={handleSubmit}>
                <div className="form-row">
                    <input
                        type="text"
                        placeholder="Backup Schedule"
                        value={backupSchedule}
                        onChange={(e) => setBackupSchedule(e.target.value)}
                        required
                    />
                </div>
                <div className="form-row">
                    <input
                        type="date"
                        placeholder="Last Backup Date"
                        value={lastBackupDate}
                        onChange={(e) => setLastBackupDate(e.target.value)}
                        required
                    />
                </div>
                 <div className="form-row">
                     <input
                        type="text"
                        placeholder="Recovery Point Objective"
                        value={recoveryPointObjective}
                        onChange={(e) => setRecoveryPointObjective(e.target.value)}
                        required
                    />
                </div>
                 <div className="form-row">
                     <input
                        type="text"
                        placeholder="Recovery Time Objective"
                        value={recoveryTimeObjective}
                        onChange={(e) => setRecoveryTimeObjective(e.target.value)}
                        required
                    />
                </div>
                 <div className="form-row">
                    <input
                        type="text"
                        placeholder="Backup Location"
                        value={backupLocation}
                        onChange={(e) => setBackupLocation(e.target.value)}
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

export default BackupRecoveryForm; 