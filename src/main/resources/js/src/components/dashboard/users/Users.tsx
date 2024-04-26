import Loader from "../../utility/Loader";
import { Link, useNavigate, useOutletContext } from "react-router-dom";
import { DashboardContextType } from "../Dashboard";
import CustomPieChart from "../../charts/piechart";
import { navbarHeight } from "../../navbar/Navbar";
import { useEffect, useState } from "react";
import { SortIcons } from "../../utility/SortIcons";
import { SortState } from "../../../models/sortState";
import Unauthorized from "../../utility/Unauthorized";


export default function Users() {
    const { users, setUsers, globalError } = useOutletContext<DashboardContextType>()
    const [sortState, setSortState] = useState('UNSORTED' as SortState)
    const [sortedBy, setSortedBy] = useState('Id' as 'Id'|'Firstname'|'Lastname'|'Email'|'Roles')
    const navigate = useNavigate()

    function getDistributionOfUsers(): { name: string; value: number; }[] {
        const rolesCount: Map<string, number> = new Map();
        users.forEach((u) => {
            u.roleNames.forEach(r => {
                if (rolesCount.has(r)) {
                    rolesCount.set(r, rolesCount.get(r)! + 1)
                } else {
                    rolesCount.set(r, 1);
                }
            })
        })
        // the array is sorted because every time 'users' were sorted, the order would change and the piechart would rerender
        return Array.from(rolesCount, ([name, value]) => ({ name, value })).sort((a,b) => a.value-b.value)
    }

    //run when sorting icon is clicked
    useEffect(() => {
        const prevUsers = [...users]
        prevUsers.sort((a, b) => {
            switch(sortedBy) {
                case "Id":
                    return a.id! - b.id!
                case "Firstname":
                    return a.firstName.localeCompare(b.firstName)
                case "Lastname":
                    return a.lastName.localeCompare(b.lastName)
                case "Email":
                    return a.email.localeCompare(b.email)
                case "Roles":
                    return a.roleNames.join(',').localeCompare(b.roleNames.join(','))
            }
        })
        if(sortState == 'DESCENDING') {
            prevUsers.reverse()
        }
        setUsers(prevUsers)
    }, [sortedBy, sortState])

    const TableData = ({ value, style, sortable }: { value: string, style?: string, sortable?: boolean }) =>
        <td className={`p-2 border-x-2 text-center ${style}`}>
            <span className="flex flex-row">
                {sortable && <SortIcons property={value} sortedBy={sortedBy} setSortedBy={setSortedBy} state={sortState} setState={setSortState} />}
                {value}
            </span>
        </td>

    return (
        <div className={`flex w-4/5 h-[calc(100vw-${navbarHeight})]  p-2 flex-col gap-4`}>
            {globalError.users ? <Unauthorized /> : (users.length === 0 ?  <Loader /> : 
                <>
                    <div className="overflow-auto h-1/2 border-y-2">
                        <table className="font-lato border-collapse w-full h-full">
                            <thead>
                                <tr>
                                    {"Id,Firstname,Lastname,Email,Roles".split(',').map(d => <TableData value={d} key={d} sortable style="sticky top-0 z-[1] font-bold bg-white " />)}
                                </tr>
                            </thead>
                            <tbody>
                                {users.map(u => {
                                    return (
                                        <tr key={u.id} className="hover:bg-gray-400 transition-all duration-75 cursor-pointer [&>*]:font-normal h-2" onClick={() => navigate(`../users/${u.id}`)}>
                                            <TableData value={u.id?.toString() ?? ''} />
                                            <TableData value={u.firstName} />
                                            <TableData value={u.lastName} />
                                            <TableData value={u.email} />
                                            <TableData value={u.roleNames?.join(',')} />
                                        </tr>
                                    )
                                })}
                            </tbody>
                        </table>
                    </div>
                    <div className="h-1/2 w-full flex">
                        <CustomPieChart data={getDistributionOfUsers()} />
                        <div className="w-full">
                            <h1 className="text-3xl">Users activities</h1>
                            <div className="flex flex-col gap-2 pt-2">
                                <p className="w-full border-2 h-12 flex items-center p-2">
                                    User&nbsp;<Link to='13' className="text-blue-500">Jan Kowalski</Link>
                                    &nbsp;has successfully created an&nbsp;<Link to='../items/12' className="text-blue-500">Item</Link>
                                    &nbsp;'Monitor'
                                </p>
                                <p className="w-full border-2 h-12 flex items-center p-2">
                                    User&nbsp;<Link to='13' className="text-blue-500">Jan Kowalski</Link>
                                    &nbsp;has successfully created an&nbsp;<Link to='../items/12' className="text-blue-500">Item</Link>
                                    &nbsp;'Monitor'
                                </p>
                                <p className="w-full border-2 h-12 flex items-center p-2">
                                    User&nbsp;<Link to='13' className="text-blue-500">Jan Kowalski</Link>
                                    &nbsp;has successfully created an&nbsp;<Link to='../items/12' className="text-blue-500">Item</Link>
                                    &nbsp;'Monitor'
                                </p>
                                <p className="w-full border-2 h-12 flex items-center p-2 bg-red-200">
                                    User&nbsp;<Link to='13' className="text-blue-500">Adam Nowak</Link>
                                    &nbsp;was&nbsp;<span className="text-red-600 underline">unauthorized</span>&nbsp;when trying to access&nbsp;<Link to='../users' className="text-blue-500">Users</Link>
                                </p>
                            </div>
                            <h2 className="text-xl text-gray-500 text-center pt-4">There was no more activity today.</h2>
                        </div>
                    </div>
                </>
            )}
        </div>
    )
}