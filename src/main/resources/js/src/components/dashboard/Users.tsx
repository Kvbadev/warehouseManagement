import { useEffect, useState } from "react";
import { User } from "../../models/user";
import { api } from "../../api/apiClient";
import { toast } from "react-toastify";
import Loader from "./Loader";
import { useOutletContext } from "react-router-dom";
import { DashboardContextType } from "./Dashboard";

export default function Users() {
    const {users} = useOutletContext<DashboardContextType>();

    return (
        <div className="flex w-4/5 h-full p-2 flex-col gap-4">
            <table className="font-lato border-collapse w-full">
                <thead>
                    <tr>
                        <th className="border-[1px] p-2">Id</th>
                        <th className="border-[1px] p-2">Firstname</th>
                        <th className="border-[1px] p-2">Lastname</th>
                        <th className="border-[1px] p-2">Email</th>
                        <th className="border-[1px] p-2">Roles</th>
                    </tr>
                </thead>
                <tbody>
                    {users.map(u => {
                        return (
                            <tr key={u.id}>
                                <td className="border-[1px] p-2">{u.id}</td>
                                <td className="border-[1px] p-2">{u.firstName}</td>
                                <td className="border-[1px] p-2">{u.lastName}</td>
                                <td className="border-[1px] p-2">{u.email}</td>
                                <td className="border-[1px] p-2">{u.roles.join(', ')}</td>
                            </tr>
                        )
                    })}
                </tbody>
            </table>
            {users.length === 0 && <Loader />}
        </div>
    )
}