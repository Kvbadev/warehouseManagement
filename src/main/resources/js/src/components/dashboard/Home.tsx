import { Link, useOutletContext } from "react-router-dom";
import SimpleInfoBlock from "./SimpleInfoBlock";
import { TableItemInfoBlock, TableDeliveryInfoBlock } from "./TableInfoBlock";
import { DashboardContextType } from "./Dashboard";
import { useLoggedUser } from "../../context/LoggedUserContext";
import { useEffect, useState } from "react";
import Item from "../../models/item";
import Delivery from "../../models/delivery";

export default function Home() {
    const { items, deliveries, globalError, itemsTotalCount, itemsTotalPrice, deliveriesTotalDelayed } = useOutletContext<DashboardContextType>()
    const [shortenedItems, setShortenedItems] = useState([] as Item[])
    const [shortenedDeliveries, setShortenedDeliveries] = useState([] as Delivery[])
    const {loggedUser} = useLoggedUser();

    useEffect(() => {
        setShortenedItems(items.slice(0,10))
        setShortenedDeliveries(deliveries.slice(0,10))
    }, [items,deliveries])

    return (
        <div className="max-w-6xl mx-auto my-0 w-4/5 p-4">
            <div className="flex flex-col pb-2">
                <h1 className="text-3xl font-lato">Dashboard</h1>
                <p className="text-lg font-lato text-red-800">Welcome <Link className="text-orange-400" to='../../profile'>{loggedUser?.firstName}</Link></p>
            </div>
            <div className="flex flex-col gap-4 justify-between">
                <div className="flex flex-row gap-4 w-full">
                    <SimpleInfoBlock digit={itemsTotalCount.toString()} label="Items in stock" link="../items" linkLabel="Show all" />
                    {/* <SimpleInfoBlock digit="33" label="New deliveries" /> */}
                    <SimpleInfoBlock digit={(deliveriesTotalDelayed ?? 0).toString()} label="Delayed deliveries" link="../deliveries?delayed=true" linkLabel="Resolve all" isNegative />
                    <SimpleInfoBlock digit={'$' + Intl.NumberFormat('en', { notation: 'compact', maximumSignificantDigits: 4}).format(itemsTotalPrice)} label="Total Worth" />
                </div>
                <div className="flex flex-row gap-4 w-full">
                    <TableItemInfoBlock error={globalError.items} name="Items" items={shortenedItems} />
                    <TableDeliveryInfoBlock error={globalError.deliveries} name="Deliveries" deliveries={shortenedDeliveries} />
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