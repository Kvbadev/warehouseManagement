import { Link, useOutletContext } from "react-router-dom";
import SimpleInfoBlock from "./SimpleInfoBlock";
import { TableItemInfoBlock, TableDeliveryInfoBlock } from "./TableInfoBlock";
import { DashboardContextType } from "./Dashboard";
import { useLoggedUser } from "../../context/LoggedUserContext";

export default function Home() {
    const { items, deliveries } = useOutletContext<DashboardContextType>()
    const {loggedUser} = useLoggedUser();

    return (
        <div className="max-w-6xl mx-auto my-0 w-4/5 p-4">
            <div className="flex flex-col pb-2">
                <h1 className="text-3xl font-lato">Dashboard</h1>
                <p className="text-lg font-lato text-red-800">Welcome <Link className="text-orange-400" to='../../profile'>{loggedUser?.firstName}</Link></p>
            </div>
            <div className="flex flex-col gap-4 justify-between">
                <div className="flex flex-row gap-4 w-full">
                    <SimpleInfoBlock digit="553" label="Items in stock" link="../items" linkLabel="Show all" />
                    <SimpleInfoBlock digit="33" label="New deliveries" />
                    <SimpleInfoBlock digit="4" label="Delayed deliveries" link="../deliveries?delayed=true" linkLabel="Resolve all" isNegative />
                    <SimpleInfoBlock digit={'$22.3k'} label="Total Worth" />
                </div>
                <div className="flex flex-row gap-4 w-full">
                    <TableItemInfoBlock name="Items" items={items} />
                    <TableDeliveryInfoBlock name="Deliveries" deliveries={deliveries} />
                </div>
                <div className="flex flex-col bg-gray-400 h-24 p-2">
                    <h1 className="text-xl text-black">Latest errors</h1>
                    <p className="text-red-600">Request failed with status code 403</p>
                    <p className="text-red-600">Request failed with status code 404 - Not Found</p>
                </div>
            </div>
        </div>
    )
}