export interface Registration {
    firstName: string,
    lastName: string,
    email: string,
    username: string,
    password: string,
    confirmPassword: string,
    userType: TypeUser
}

export enum TypeUser{
    PERSONAL_USER,
    BUSSINESS_USER,
    ADMIN
}