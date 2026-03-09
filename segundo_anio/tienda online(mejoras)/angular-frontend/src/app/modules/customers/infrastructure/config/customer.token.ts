import { InjectionToken } from "@angular/core";
import { CustomerRepository } from "../../domain/customer.repository";

export const CUSTOMER_REPOSITORY = new InjectionToken<CustomerRepository>('CUSTOMER_REPOSITORY');