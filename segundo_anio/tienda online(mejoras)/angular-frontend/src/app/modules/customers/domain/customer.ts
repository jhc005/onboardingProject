export interface Customer {
  id?: number;
  firstName: string;
  lastName: string;
  email: string;
  phone: string;
  registrationDate?: string;
  active?: boolean;
}