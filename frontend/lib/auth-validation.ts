export type Credentials = { email: string; password: string };
export type RegistrationDetails = Credentials & { name: string };

const properName = /^[A-Z][a-zA-Z]*(?: [A-Z][a-zA-Z]*)*$/;
const emailAddress = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
const numericPassword = /^\d{6,}$/;

export function registrationValidationMessage(details: RegistrationDetails): string | null {
  if (!properName.test(details.name)) {
    return "Enter your full name using letters only, with each name starting with a capital letter. Do not start with a number or symbol.";
  }
  return credentialsValidationMessage(details);
}

export function credentialsValidationMessage(details: Credentials): string | null {
  if (!emailAddress.test(details.email)) {
    return "Enter a valid email address that you can access.";
  }
  if (!numericPassword.test(details.password)) {
    return "Enter a password with at least 6 digits and numbers only.";
  }
  return null;
}
