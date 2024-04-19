import { Link } from "react-router-dom";
import Item from "../../models/item";
import Loader from "./Loader";
import Delivery from "../../models/delivery";

export function TableItemInfoBlock({ name, items }: { name: string, items: Item[] }) {
    return (
        <div className="flex flex-col w-full bg-gray-200 rounded-tl-xl rounded-br-xl min-h-[305px]">
            <h1 className="font-lato text-4xl pt-2 w-full text-center">{name}</h1>
            <div className="flex flex-col flex-1 p-4 font-lato text-gray-800 ">
                <div className="flex flex-row [&>p]:font-bold">
                    <p className="flex-1">Id</p>
                    <p className="flex-1">Name</p>
                    <p className="flex-1">Price</p>
                    <p className="flex-1">Parcel</p>
                </div>
                <hr className="h-px m-1 bg-gray-200 border-0 dark:bg-gray-500" />
                {items.length === 0 && <Loader />}
                {items.map(i => {
                    const itemParcel = i._links.parcel ? i._links.parcel?.href : '';
                    const parcelName = itemParcel.substring(itemParcel.lastIndexOf("/parcels/") + 1) ?? null;

                    return (
                        <div key={i.id} className="flex flex-row">
                            <p className="flex-1">{i.id}</p>
                            <p className="flex-1">
                                <Link to={`../items/${i.id}`} className="hover:text-orange-600">{i.name}</Link>
                            </p>
                            <p className="flex-1">${i.netPrice}</p>
                            <p className="flex-1">
                                {parcelName !== '' ?
                                    <Link to={`../${parcelName}`} className="hover:text-orange-600">
                                        {parcelName}
                                    </Link>
                                    : <ParcelNotSet itemId={i.id} />}
                            </p>
                            {/* <hr className="h-px m-0.5 bg-gray-200 border-0 dark:bg-gray-500" /> */}
                        </div>
                    )
                })}
            </div>
        </div>
    )
}

function ParcelNotSet({ itemId }: { itemId: number }) {
    return (
        <Link to={`../items/${itemId}`} className="text-gray-500 underline hover:text-orange-600">Not set</Link>
    )
}


export function TableDeliveryInfoBlock({ name, deliveries }: { name: string, deliveries: Delivery[] }) {
    const isDelayed = (date: string, hasArrived: boolean) => {
        if (hasArrived) return false;
        const todayDateString = new Date().toISOString().slice(0, 10);
        const todayDate = Date.parse(todayDateString);
        const arrivalDate = Date.parse(date)

        if (todayDate > arrivalDate) return true;
        return false;
    }
    return (
        <div className="flex flex-col w-full bg-gray-200 rounded-tl-xl rounded-br-xl min-h-[305px]">
            <h1 className="font-lato text-4xl pt-2 w-full text-center">{name}</h1>
            <div className="flex flex-col flex-1 p-4 font-lato text-gray-800 ">
                <div className="flex flex-row [&>p]:font-bold">
                    <p className="flex-1">Id</p>
                    <p className="flex-1">Estimated Arrival</p>
                    <p className="flex-1">Has Arrived</p>
                </div>
                <hr className="h-px m-1 bg-gray-200 border-0 dark:bg-gray-500" />
                {deliveries.length === 0 && <Loader />}
                {deliveries.map(d => {
                    return (
                        <Link to={`../deliveries/${d.id}`} key={d.id} className="flex flex-row hover:bg-gray-500">
                            <p className="flex-1">{d.id}</p>
                            <p data-delayed="delayed!" className={`flex-1 relative ${isDelayed(d.arrivalDate, d.hasArrived) && 'text-red-600 after:content-[attr(data-delayed)] after:underline after:pl-2 after:opacity-50'}`}>
                                {d.arrivalDate}
                            </p>
                            <p className="flex-1">{d.hasArrived ? 'Yes' : 'No'}</p>
                        </Link>
                    )
                })}
            </div>
        </div>
    )
}