import {AbstractControl, ValidationErrors} from '@angular/forms';

export class UserValidators{

  static cannotContainSpace(control: AbstractControl): ValidationErrors | null{
    if ( (control.value as string).indexOf(' ') >= 0){
      return  {cannotContainSpace: true};
    }
    else {
      return null;
    }
  }

  static emailPatternMatch(control: AbstractControl): ValidationErrors | null{
    const regexpEmail = new RegExp(/^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/);
    const isMatched = regexpEmail.test((control.value as string));
    if (!isMatched){
      return {emailPatternMatch: true};
    }
    else{
      return null;
    }
  }

  /**
   * Compares the values of the password controls inside the given form.
   * It returns {passwordsValuesNotMatching: true} of type {ValidationErrors} if the passwords controls values do not match.
   * Otherwise it returns null.
   * @param formControl - The form control ( must contain two fields named "password" && "confirmPassword" )
   */
  static passwordsValuesCompare(formControl: AbstractControl): ValidationErrors | null{
    const p1Value = formControl.get('password').value as string;
    const p2Value = formControl.get('confirmPassword').value as string;
    if (p1Value !== p2Value){
      return {passwordsValuesNotMatching: true};
    }
    else {
      return null;
    }
  }
}
