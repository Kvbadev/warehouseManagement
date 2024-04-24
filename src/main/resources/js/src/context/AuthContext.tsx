import { createContext, useContext, useEffect, useMemo, useState } from "react";

const AuthContext = createContext({} as {token: string, setToken: (newToken: string) => void})

export const AuthProvider = ({children}: {children: any}) => {

    const [token, setToken_] = useState(localStorage.getItem('token') ?? "")

    const setToken = (newToken: string) => {
        setToken_(newToken);
    }

    useEffect(() => {
        if (token) {
            localStorage.setItem('token', token);
        } 
    }, [token])

    const contextValue = useMemo(
        () => ({
            token,
            setToken
        }),
        [token]
    )

    return (
        <AuthContext.Provider value={contextValue}>{children}</AuthContext.Provider>
    )
}

export const useAuth = () => {
    return useContext(AuthContext);
};
