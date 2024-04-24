import { ReactNode } from "react";
import { FaHome, FaUser } from "react-icons/fa";
import { GoPackage } from "react-icons/go";
import { TbSitemap, TbTruckDelivery } from "react-icons/tb";
import { Link, useLocation } from "react-router-dom";

enum Nav {
  home,
  users,
  items,
  parcels,
  deliveries
};

const getDashboardTab = (pathname: string) => {
  const indicesOfSlash = [];
  let tab = '';

  for(const [index, char] of Object.entries(pathname)) {
    if(char === '/') indicesOfSlash.push(parseInt(index));
  }

  if(indicesOfSlash.length === 2) { // it means a url like /dashboard/items or /dashboard/parcels but not /dashboard/users/1
    tab = pathname.slice(indicesOfSlash[1]+1);
  } 
  else if(indicesOfSlash.length === 3) {
    
    tab = pathname.slice(
      indicesOfSlash[1]+1, indicesOfSlash[2]
    ) // for urls like /dashboard/parcels/3 as it gets the value between the second and the last slash
  }

  return tab;
}

export default function Sidebar() {
  const {pathname} = useLocation();
  const dashboardTab = getDashboardTab(pathname);

  const SideBarMenuItem = ({ tab, icon }: { tab: Nav, icon: ReactNode }) => {
    return (
      <Link to={Nav[tab]} className={`text-lg flex items-center py-4 px-2 transition-all ${Nav[tab] === dashboardTab && 'bg-gray-300'} hover:text-orange-600`}>
        {icon}
        <p className="pl-3 text-xl capitalize">{Nav[tab]}</p>
      </Link>
    )
  }
  return (
    <div className="h-screen bg-gray-200 w-1/5 flex flex-col">
      <SideBarMenuItem tab={Nav.home} icon={<FaHome size={24} />} />
      <SideBarMenuItem tab={Nav.users} icon={<FaUser size={24} />} />
      <SideBarMenuItem tab={Nav.items} icon={<TbSitemap size={24} />} />
      <SideBarMenuItem tab={Nav.parcels} icon={<GoPackage size={24} />} />
      <SideBarMenuItem tab={Nav.deliveries} icon={<TbTruckDelivery size={24} />} />
    </div>
  )

}