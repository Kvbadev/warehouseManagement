import Loader from "../../utility/Loader";
import { Link, useNavigate, useOutletContext } from "react-router-dom";
import { DashboardContextType } from "../Dashboard";
import CustomPieChart from "../../charts/piechart";
import { navbarHeight } from "../../navbar/Navbar";
import { useEffect, useState } from "react";
import { SortIcons } from "../../utility/SortIcons";
import { SortState } from "../../../models/sortState";
import Unauthorized from "../../utility/Unauthorized";


export default function Deliveries() {
    const { deliveries, setDeliveries, globalError } = useOutletContext<DashboardContextType>()
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
    function camelize(str: string): 'id'|'arrivalDate'|'hasArrived' {
        return str.replace(/(?:^\w|[A-Z]|\b\w)/g, function(word, index) {
          return index === 0 ? word.toLowerCase() : word.toUpperCase();
        }).replace(/\s+/g, '') as any;
    }

    //run when sorting icon is clicked
    useEffect(() => {
        const prevDeliverys = [...deliveries]
        prevDeliverys.sort((a, b) => {
            return compareTo(a[camelize(sortedBy)], b[camelize(sortedBy)]);
        })
        if(sortState == 'DESCENDING') {
            prevDeliverys.reverse()
        }
        setDeliveries(prevDeliverys)
    }, [sortedBy, sortState])

    const TableData = ({ value, style, sortable }: { value: string, style?: string, sortable?: boolean }) =>
        <td className={`p-2 border-x-2 text-center h-8 ${style}`}>
            <span className="flex flex-row">
                {sortable && <SortIcons property={value} sortedBy={sortedBy} setSortedBy={setSortedBy} state={sortState} setState={setSortState} />}
                {value}
            </span>
        </td>

    return (
        <div className={`flex w-4/5 h-[calc(100vw-${navbarHeight})]  p-2 flex-col gap-4`}>
                {globalError.deliveries ? <Unauthorized /> : (deliveries.length === 0 ?  <Loader /> : 
                    <div className="overflow-auto h-full border-y-2">
                        <table className="font-lato border-collapse w-full h-full">
                            <thead>
                                <tr>
                                    {"Id,Arrival Date,Arrived".split(',').map(d => <TableData value={d} key={d} sortable style="sticky top-0 z-[1] font-bold bg-white " />)}
                                </tr>
                            </thead>
                            <tbody>
                                {deliveries.map(u => {
                                    return (
                                        <tr key={u.id} className="hover:bg-gray-400 transition-all duration-75 cursor-pointer [&>*]:font-normal h-2 border min-h-10" onClick={() => navigate(`../deliveries/${u.id}`)}>
                                            <TableData value={u.id?.toString() ?? ''} />
                                            <TableData value={u.arrivalDate} />
                                            <TableData value={u.hasArrived ? 'Yes':'No' } />
                                        </tr>
                                    )
                                })}
                            </tbody>
                        </table>
                    </div>
            )}
        </div>
    )
}