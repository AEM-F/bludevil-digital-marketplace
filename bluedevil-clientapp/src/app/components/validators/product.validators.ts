import {AbstractControl, ValidationErrors} from "@angular/forms";

export class ProductValidators{
  static positiveNumberPattern = '^\\d+(.\\d{1,2})?';
  static datePattern = '^\\d{4}\\/(0[1-9]|1[012])\\/(0[1-9]|[12][0-9]|3[01])$';
  /**
   *Checking if there is a space in the input
   * @param control -the control obj used to retrieve the user input
   */
  static cannotContainSpace(control: AbstractControl): ValidationErrors | null{
    if ( (control.value as string).indexOf(' ') >= 0){
      return  {cannotContainSpace: true};
      return null;
    }
  }

}
