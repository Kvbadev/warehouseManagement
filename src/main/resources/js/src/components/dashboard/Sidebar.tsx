import { ReactNode, useState } from "react";
import { FaHome, FaUser } from "react-icons/fa";
import { GoPackage } from "react-icons/go";
import { TbSitemap, TbTruckDelivery } from "react-icons/tb";
import { Link } from "react-router-dom";

export default function Sidebar() {
  const [selectedNav, setSelectedNav] = useState('home');

  const SideBarMenuItem = ({ text, icon}: { text: string, icon: ReactNode}) => {
    return (
      <Link to={text.toLowerCase()} onClick={() => setSelectedNav(s => text.toLowerCase())} className={`text-lg flex items-center py-4 px-2 transition-all ${text === selectedNav && 'bg-gray-300'} hover:text-orange-600`}>
        {icon}
        <p className="pl-3 text-xl">{text}</p>
      </Link>
    )
  }
    return (
        <div className="h-screen bg-gray-200 w-1/5 flex flex-col">
        <SideBarMenuItem text="Home" icon={<FaHome size={24} />} />
        <SideBarMenuItem text="Users" icon={<FaUser size={24} />} />
        <SideBarMenuItem text="Items" icon={<TbSitemap size={24} />} />
        <SideBarMenuItem text="Parcels" icon={<GoPackage size={24} />} />
        <SideBarMenuItem text="Deliveries" icon={<TbTruckDelivery size={24} />} />
      </div>
    )

}