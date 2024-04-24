import { createContext, useContext, useEffect, useMemo, useState } from "react";
import { User } from "../models/user";

const LoggedUserContext = createContext({} as {loggedUser?: User, setLoggedUser: (newUser?: User) => void})

export const LoggedUserProvider = ({children}: {children: any}) => {

    const [loggedUser, setLoggedUser] = useState(() => {
            const userFromLocal = localStorage.getItem('loggedUser')
            return !userFromLocal || userFromLocal === 'undefined' ?
            null
            : JSON.parse(userFromLocal)
        }
    )

    const contextValue = useMemo(
        () => ({
            loggedUser,
            setLoggedUser
        }),
        [loggedUser]
    )

    return (
        <LoggedUserContext.Provider value={contextValue}>{children}</LoggedUserContext.Provider>
    )
}

export const useLoggedUser = () => {
    return useContext(LoggedUserContext);
};