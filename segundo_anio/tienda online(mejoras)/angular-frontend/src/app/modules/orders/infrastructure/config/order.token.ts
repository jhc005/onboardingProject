import { InjectionToken } from "@angular/core";
import { OrderRepository } from "../../domain/order.repository";

export const ORDER_REPOSITORY = new InjectionToken<OrderRepository>('ORDER_REPOSITORY')