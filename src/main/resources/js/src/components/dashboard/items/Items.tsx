import Loader from "../../utility/Loader";
import { Link, useNavigate, useOutletContext } from "react-router-dom";
import { DashboardContextType } from "../Dashboard";
import CustomPieChart from "../../charts/piechart";
import { navbarHeight } from "../../navbar/Navbar";
import { useEffect, useState } from "react";
import { SortIcons } from "../../utility/SortIcons";
import { SortState } from "../../../models/sortState";
import Unauthorized from "../../utility/Unauthorized";


export default function Items() {
    const { items, setItems, globalError } = useOutletContext<DashboardContextType>()
    const [sortState, setSortState] = useState('UNSORTED' as SortState)
    const [sortedBy, setSortedBy] = useState('id' as 'id'|'name'|'description'|'quantity'|'netPrice')
    const navigate = useNavigate()

    function compareTo(a:any, b:any) {
        if (typeof a === 'number' && typeof b === 'number') {
            return a - b;
        } else if (typeof a === 'string' && typeof b === 'string') {
            return a.localeCompare(b);
        } else {
            throw new Error('Unsupported types');
        }
    }
    //TODO: fix typescript here
    function camelize(str: string): 'id'|'name'|'description'|'quantity'|'netPrice' {
        return str.replace(/(?:^\w|[A-Z]|\b\w)/g, function(word, index) {
          return index === 0 ? word.toLowerCase() : word.toUpperCase();
        }).replace(/\s+/g, '') as any;
    }

    //run when sorting icon is clicked
    useEffect(() => {
        const prevItems = [...items]
        prevItems.sort((a, b) => {
            return compareTo(a[camelize(sortedBy)], b[camelize(sortedBy)]);
        })
        if(sortState == 'DESCENDING') {
            prevItems.reverse()
        }
        setItems(prevItems)
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
                {globalError.items ? <Unauthorized /> : (items.length === 0 ?  <Loader /> : 
                <>
                    <div className="overflow-auto h-1/2 border-y-2">
                        <table className="font-lato border-collapse w-full h-full">
                            <thead>
                                <tr>
                                    {"Id,Name,Description,Quantity,Net Price".split(',').map(d => <TableData value={d} key={d} sortable style="sticky top-0 z-[1] font-bold bg-white " />)}
                                </tr>
                            </thead>
                            <tbody>
                                {items.map(u => {
                                    return (
                                        <tr key={u.id} className="hover:bg-gray-400 transition-all duration-75 cursor-pointer [&>*]:font-normal h-2" onClick={() => navigate(`../items/${u.id}`)}>
                                            <TableData value={u.id?.toString() ?? ''} />
                                            <TableData value={u.name} />
                                            <TableData value={u.description} />
                                            <TableData value={u.quantity.toString()} />
                                            <TableData value={`$${u.netPrice}`} />
                                        </tr>
                                    )
                                })}
                            </tbody>
                        </table>
                    </div>
                    <div className="h-1/2 w-full flex">
                        <CustomPieChart />
                        <div className="w-full">
                            <h1 className="text-3xl">Items activities</h1>
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
                                    &nbsp;was&nbsp;<span className="text-red-600 underline">unauthorized</span>&nbsp;when trying to access&nbsp;<Link to='../items' className="text-blue-500">items</Link>
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