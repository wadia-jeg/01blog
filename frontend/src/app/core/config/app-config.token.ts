import { InjectionToken } from "@angular/core";
import { environment } from "../../../environements.ts/environment";

export const APP_CONFIG = new InjectionToken<typeof environment>('Application config');