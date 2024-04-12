import Loader from "../Loader";
import { useNavigate, useOutletContext } from "react-router-dom";
import { DashboardContextType } from "../Dashboard";

export default function Users() {
    const {users} = useOutletContext<DashboardContextType>();
    const navigate = useNavigate()

    const TableData = ({value}: {value: string}) => 
        <th className="border-[1px] p-2">{value}</th>

    return (
        <div className="flex w-4/5 h-full p-2 flex-col gap-4">
            <table className="font-lato border-collapse w-full">
                <thead>
                    <tr>
                        {"Id,Firstname,Lastname,Email,Roles".split(',').map(d => <TableData value={d} key={d} />)}
                    </tr>
                </thead>
                <tbody>
                    {users.map(u => {
                        return (
                            <tr key={u.id} className="hover:bg-gray-400 transition-all duration-75 cursor-pointer [&>*]:font-normal" onClick={() => navigate(`../users/${u.id}`)}>
                                <TableData value={u.id?.toString() ?? ''} />
                                <TableData value={u.firstName} />
                                <TableData value={u.lastName} />
                                <TableData value={u.email} />
                                <TableData value={u.roleNames.join(',')} />
                            </tr>
                        )
                    })}
                </tbody>
            </table>
            {users.length === 0 && <Loader />}
        </div>
    )
}