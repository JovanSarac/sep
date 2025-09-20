// src/app/services/user-helper.service.ts
import { Injectable } from '@angular/core';
import { KeycloakService } from './keycloakservice';
import { User } from '../infrastructure/auth/model/user.model';

@Injectable({
  providedIn: 'root'
})
export class UserHelperService {

  constructor(private keycloakService: KeycloakService) {}

  getCurrentUser(): User | null {
    if (!this.keycloakService.isAuthenticated()) {
      return null;
    }

    const idToken = this.keycloakService.getIdTokenParsed();
    const username = this.keycloakService.getUsername();
    
    return {
      id: idToken?.id || 0,
      username: username || '',
      role: this.extractUserRole(idToken),
    };
  }

  private extractUserRole(idToken: any): string {
    const realmRoles = idToken?.realm_access?.roles;
    
    if (realmRoles && realmRoles.length > 0) {
        if (realmRoles.includes('ROLE_WEB_SHOP')) {
        return 'ROLE_WEB_SHOP';
      }
      // Traži specifične uloge
      if (realmRoles.includes('ROLE_BUSINESS_USER')) {
        return 'ROLE_BUSINESS_USER';
      }
      if (realmRoles.includes('ROLE_PERSONAL_USER')) {
        return 'ROLE_PERSONAL_USER';
      }
      if (realmRoles.includes('ROLE_ADMIN')) {
        return 'ROLE_ADMIN';
      }
      if (realmRoles.includes('ROLE_USER')) {
        return 'ROLE_USER';
      }
      
      const userRoles = realmRoles.filter((role: string) => 
        !['offline_access', 'uma_authorization', 'default-roles-sep-realm'].includes(role)
      );
      return userRoles[0] || 'ROLE_PERSONAL_USER';
    }
    
    return 'ROLE_PERSONAL_USER';
  }

  getUserDisplayName(): string {
    const user = this.getCurrentUser();
    return user?.username || 'User';
  }

  hasRole(role: string): boolean {
    const user = this.getCurrentUser();
    return user?.role === role;
  }

  isBusinessUser(): boolean {
    return this.hasRole('ROLE_BUSINESS_USER');
  }

  isPersonalUser(): boolean {
    return this.hasRole('ROLE_PERSONAL_USER');
  }

  isAdmin(): boolean {
    return this.hasRole('ROLE_ADMIN');
  }
}