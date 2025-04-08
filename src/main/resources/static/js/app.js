'use strict';

const signIn = function () {
    class Validator {
        /**
         * Initialize Validator object for given form element.
         *
         * @param formElement
         * @param options
         */
        constructor(formElement, options) {
            if (typeof options === 'undefined') {
                // Default options.
                options = {};
            }

            this._formElement = formElement;

            // Validation rules.
            this._validators = typeof options.validators !== 'undefined' ? options.validators : {};

            // Form submission event handler.
            this._onSubmitEvent = typeof options.onSubmitEventHandler === 'function'
                ? options.onSubmitEventHandler.bind(this)
                : function (event) {
                    if (!this.isValidForm()) {
                        event.preventDefault();

                        this.setFocusOnFirstInvalidField();
                    }
                }.bind(this);

            // Form field input event handler.
            this._inputEvent = function (event) {
                this._validateElement(event.target);
            }.bind(this);

            const elements = this.getElements();

            // Add form field input event handler.
            for (let i = 0; i < elements.length; i++) {
                elements.item(i).addEventListener('input', this._inputEvent);
            }

            // Add form submission event handler.
            this.getFormSubmitButton().addEventListener('click', this._onSubmitEvent);
        }

        /**
         * Remove all validation event listeners, on form elements.
         */
        destroy() {
            const elements = this.getElements();

            // Remove form field input event handler.
            for (let i = 0; i < elements.length; i++) {
                elements.item(i).removeEventListener('input', this._inputEvent);
            }

            // Remove form submission event handler.
            this.getFormSubmitButton().removeEventListener('click', this._onSubmitEvent);
        }

        /**
         * Set focus on first form field.
         */
        setFocusOnFirstField() {
            const elements = this.getElements();

            if (elements.length > 0) {
                elements.item(0).focus();
            }
        }

        /**
         * Set focus on first invalid form field.
         */
        setFocusOnFirstInvalidField() {
            const elements = this.getElements();

            for (let i = 0; i < elements.length; i++) {
                let element = elements.item(i);

                if (this._isInvalid(element)) {
                    element.focus();

                    return;
                }
            }
        }

        /**
         * Set errors.
         *
         * @param errors
         */
        setErrors(errors) {
            const elements = this.getElements();

            for (let i = 0; i < elements.length; i++) {
                let element = elements.item(i),
                    name = element.getAttribute('name');

                if (typeof errors[name] !== 'undefined') {
                    for (let i = 0; i < errors[name].length; i++) {
                        this._setInvalid(element, errors[name][i]);
                    }
                }
            }
        }

        /**
         * Validate form.
         *
         * @param except
         */
        validateForm(except) {
            if (typeof except === 'undefined') {
                except = [];
            }

            const elements = this.getElements();

            for (let i = 0; i < elements.length; i++) {
                let element = elements.item(i);

                if (except.length === 0 || except.indexOf(element.getAttribute('name')) === -1) {
                    this._validateElement(element);
                }
            }
        }

        /**
         * Validate form field by field name.
         *
         * @param name
         */
        validateField(name) {
            const element = this.getElement(name);

            if (typeof element !== 'undefined') {
                this._validateElement(element);
            }
        }

        /**
         * Check if form is valid.
         *
         * @returns {boolean}
         */
        isValidForm() {
            if (!this.isValidatedForm()) {
                this.validateForm();
            }

            const elements = this.getElements();

            for (let i = 0; i < elements.length; i++) {
                if (this._isInvalid(elements.item(i))) {
                    return false;
                }
            }

            return true;
        }

        /**
         * Check if field of form is valid.
         *
         * @param name
         * @returns {*}
         */
        isValidField(name) {
            const element = this.getElement(name);

            if (typeof element !== 'undefined') {
                if (!this._isValidated(element)) {
                    this._validateElement(element);
                }

                return this._isValid(element);
            }
        }

        /**
         * Check if form was validated.
         *
         * @returns {boolean}
         */
        isValidatedForm() {
            const elements = this.getElements();

            for (let i = 0; i < elements.length; i++) {
                if (!this._isValidated(elements.item(i))) {
                    return false;
                }
            }

            return true;
        }

        /**
         * Check if field of form was validated.
         *
         * @param name
         * @returns {boolean}
         */
        isValidatedField(name) {
            const element = this.getElement(name);

            if (typeof element === 'undefined') {
                return this._isValidated(element);
            }
        }

        /**
         * Set field of form as valid.
         *
         * @param name
         * @param message
         */
        setValidField(name, message) {
            const element = this.getElement(name);

            if (typeof element !== 'undefined') {
                this._setValid(element, message);
            }
        }

        /**
         * Set field of form as invalid.
         *
         * @param name
         * @param message
         */
        setInvalidField(name, message) {
            const element = this.getElement(name);

            if (typeof element !== 'undefined') {
                this._setInvalid(element, message);
            }
        }

        /**
         * Clear all validation on field of form.
         *
         * @param name
         */
        clearFieldValidation(name) {
            const element = this.getElement(name);

            if (typeof element !== 'undefined') {
                this._clearValidation(element);
            }
        }

        /**
         * Get validation form element.
         *
         * @returns {*}
         */
        getFormElement() {
            return this._formElement;
        }

        /**
         * Get form submit button.
         *
         * @returns {any}
         */
        getFormSubmitButton() {
            return this._formElement.querySelector('button[type=submit]');
        }

        /**
         * Get form elements for validation.
         *
         * @returns {NodeListOf<HTMLElementTagNameMap[string]> | NodeListOf<Element> | NodeListOf<SVGElementTagNameMap[string]>}
         */
        getElements() {
            const supportedElements = [
                'input.form-control',
                'textarea.form-control',
                'select.form-select',
                'input.form-check-input'
            ];

            return this._formElement.querySelectorAll(supportedElements.join(','));
        }

        /**
         * Clear validation on all elements of form.
         */
        clearFormValidation() {
            const elements = this.getElements();

            for (let i = 0; i < elements.length; i++) {
                this._clearValidation(elements.item(i));
            }
        }

        /**
         * Get element of form by name.
         *
         * @param name
         * @returns {Element}
         */
        getElement(name) {
            const elements = this.getElements();

            for (let i = 0; i < elements.length; i++) {
                let element = elements.item(i);

                if (element.getAttribute('name') === name) {
                    return element;
                }
            }
        }

        /**
         * Check if field is validated.
         *
         * @param element
         * @returns {boolean}
         * @private
         */
        _isValidated(element) {
            if (!this._isValid(element) && !this._isInvalid(element)) {
                return false;
            }

            return true;
        }

        /**
         * Check if field is invalid.
         *
         * @param element
         * @returns {*}
         * @private
         */
        _isInvalid(element) {
            return element.classList.contains('is-invalid');
        }

        /**
         * Check if field is valid.
         *
         * @param element
         * @returns {*}
         * @private
         */
        _isValid(element) {
            return element.classList.contains('is-valid');
        }

        /**
         * Validate element of form.
         *
         * @param element
         * @private
         */
        _validateElement(element) {
            const validators = this._getElementValidators(element);

            let valid = true,
                message = '';

            for (let validator in validators) {
                if (!validators.hasOwnProperty(validator)) {
                    continue;
                }

                if (typeof validators[validator] === 'function') {
                    let result = (validators[validator].bind(this))(element);

                    valid = typeof result.valid === 'boolean' ? result.valid : true;
                    message = typeof result.message === 'string' ? result.message : '';

                    if (!valid) {
                        // Exit after first validation error.
                        break;
                    }
                }
            }

            valid ? this._setValid(element, message) : this._setInvalid(element, message);
        }

        /**
         * Get element of form validators.
         *
         * @param element
         * @returns {{callback: (function(*): {valid: boolean, message: string})}|*}
         * @private
         */
        _getElementValidators(element) {
            const name = element.getAttribute('name');

            if (typeof this._validators[name] !== 'undefined') {
                return this._validators[name];
            } else {
                // Default validator.
                return {
                    'callback': function (element) {
                        return {'valid': true};
                    }
                };
            }
        }

        /**
         * Set element of form as valid.
         *
         * @param element
         * @param message
         * @private
         */
        _setValid(element, message) {
            this._clearValidation(element);

            element.classList.add('is-valid');
            element.after(this._getValidFeedbackElement(message));
        }

        /**
         * Set element of form as invalid.
         *
         * @param element
         * @param message
         * @private
         */
        _setInvalid(element, message) {
            this._clearValidation(element);

            element.classList.add('is-invalid');
            element.after(this._getInvalidFeedbackElement(message));
        }

        /**
         * Clear all validation on element of form.
         *
         * @param element
         * @private
         */
        _clearValidation(element) {
            element.classList.remove('is-invalid', 'is-valid');

            const feedbacks = element.closest('div')
                .querySelectorAll('div.invalid-feedback, div.valid-feedback');

            for (let i = 0; i < feedbacks.length; i++) {
                feedbacks.item(i).remove();
            }
        }

        /**
         * Get validation valid feedback element.
         *
         * @param message
         * @returns {HTMLDivElement}
         * @private
         */
        _getValidFeedbackElement(message) {
            return this._getFeedbackElement(true, message);
        }

        /**
         * Get validation invalid feedback element.
         *
         * @param message
         * @returns {HTMLDivElement}
         * @private
         */
        _getInvalidFeedbackElement(message) {
            return this._getFeedbackElement(false, message);
        }

        /**
         * Get validation feedback element.
         *
         * @param isValid
         * @param message
         * @returns {HTMLDivElement}
         * @private
         */
        _getFeedbackElement(isValid, message) {
            if (typeof isValid !== 'boolean') {
                isValid = true;
            }

            const element = document.createElement('div');

            element.classList.add(isValid ? 'valid-feedback' : 'invalid-feedback');

            if (typeof message === 'string' && message !== '') {
                element.append(document.createTextNode(message));
            }

            return element;
        }
    }

    const validator = new Validator(document.querySelector('#login-form'), {
        'validators': {
            'username': {
                'callback': function (element) {
                    const value = element.value;

                    if (value === '') {
                        return {
                            'valid': false,
                            'message': 'поле має бути заповненим'
                        };
                    }

                    if ((new RegExp(/^[A-Za-z0-9]{1,10}$/).test(value))) {
                        return {'valid': true};
                    }

                    return {
                        'valid': false,
                        'message': value.length > 10 ? 'не більше 10 символів' : 'дозволені латинські літери і цифри'
                    };
                }
            },
            'password': {
                'callback': function (element) {
                    const value = element.value;

                    if (value === '') {
                        return {
                            'valid': false,
                            'message': 'поле має бути заповненим'
                        };
                    }

                    if ((new RegExp(/^[A-Za-z0-9]{1,10}$/).test(value))) {
                        return {'valid': true};
                    }

                    return {
                        'valid': false,
                        'message': value.length > 10 ? 'не більше 10 символів' : 'дозволені латинські літери і цифри'
                    };
                }
            }
        }
    });

    validator.setFocusOnFirstField();
};