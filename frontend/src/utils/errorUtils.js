/**
 * Error utility to manage error messages with persistence
 */

// Set the minimum time error messages should be displayed (in milliseconds)
const ERROR_DISPLAY_DURATION = 60000; // 1 minute

// Store error timeouts
const errorTimeouts = {};

/**
 * Sets an error message with a minimum display time
 * @param {Function} setErrorsFunction - The React state setter function for errors
 * @param {Object} errorData - The error data to set
 * @param {string} errorKey - A unique key for this type of error
 */
export const setPersistentError = (setErrorsFunction, errorData, errorKey = 'default') => {
  // Cancel any existing timeout for this error key
  if (errorTimeouts[errorKey]) {
    clearTimeout(errorTimeouts[errorKey]);
  }
  
  // Set the error immediately
  setErrorsFunction(errorData);
  
  // Set a timeout to clear this error after the minimum display duration
  errorTimeouts[errorKey] = setTimeout(() => {
    setErrorsFunction(prevErrors => {
      // Only clear if it's the same error (to avoid clearing newer errors)
      if (prevErrors === errorData) {
        return {};
      }
      return prevErrors;
    });
    delete errorTimeouts[errorKey];
  }, ERROR_DISPLAY_DURATION);
};

/**
 * Clears error timeouts when component unmounts
 */
export const clearErrorTimeouts = () => {
  Object.keys(errorTimeouts).forEach(key => {
    clearTimeout(errorTimeouts[key]);
  });
}; 