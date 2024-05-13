import { useEffect, useState } from "react";
import { api } from "../../api/apiClient";
import { toast } from "react-toastify";
import Item from "../../models/item";
import { useAuth } from "../../context/AuthContext";
import Sidebar from "./Sidebar";
import { Outlet } from "react-router-dom";
import Delivery from "../../models/delivery";
import { User } from "../../models/user";
import GlobalError from "../../models/globalError";
import { AxiosError } from "axios";
import Parcel from "../../models/parcel";

export type DashboardContextType = {
   items: Item[], setItems: (prev: Item[]) => void, itemsTotalCount: number, itemsTotalPrice: number,
   parcels: Parcel[], setParcels: (prev: Parcel[]) => void,
   deliveries: Delivery[], setDeliveries: (prev: Delivery[]) => void, deliveriesTotalDelayed: number,
   users: User[], setUsers: (prev: User[]) => void,
   globalError: GlobalError 
};

export function Dashboard() {

  const [items, setItems] = useState([] as Item[]);
  const [deliveries, setDeliveries] = useState([] as Delivery[]);
  const [parcels, setParcels] = useState([] as Parcel[]);
  const [users, setUsers] = useState([] as User[]);
  const [globalError, setGlobalError] = useState({} as GlobalError);
  const [itemsTotalCount, setItemsTotalCount] = useState(0)
  const [itemsTotalPrice, setItemsTotalPrice] = useState(0)
  const [deliveriesTotalDelayed, setDeliveriesTotalDelayed] = useState(0)

  const { token } = useAuth();

  useEffect(() => {
    if (!token) return;
    const getLimitItems = () => {
      api.getItems()
        .then(([res, count, price]) => {
          setItemsTotalCount(count)
          setItemsTotalPrice(price)
          res.map((i: Item) => i.netPrice = i.netPrice/100)
          setItems(res);
        })
        .catch((err: AxiosError) => {
          setGlobalError((prev: GlobalError) => {
            return {
              ...prev,
              items: err.response?.status
            }
          })
          toast(err.message, {
            type: 'error'
          })
        });
    }
    const getLimitDeliveries = () => {
      api.getDeliveries()
        .then(([res, totalDelayed]) => {
          setDeliveriesTotalDelayed(totalDelayed)
          setDeliveries(res);
        })
        .catch((err: AxiosError) => {
          setGlobalError((prev: GlobalError) => {
            return {
              ...prev,
              deliveries: err.response?.status
            }
          })
          toast(err.message, {
            type: 'error'
          })
        });
    }
    const getParcels = () => {
      api.getParcels().then(p => {
        setParcels(p);
      }).catch((err: AxiosError) => {
        setGlobalError((prev: GlobalError) => {
          return {
            ...prev,
            parcels: err.response?.status
          }
        })
        toast(err.message, {
          type: "error",
        });
      })
    }
    const getUsers = () => {
      api.getUsers().then(u => {
        setUsers(u);
      }).catch((err: AxiosError) => {
        setGlobalError((prev: GlobalError) => {
          return {
            ...prev,
            users: err.response?.status
          }
        })
        toast(err.message, {
          type: "error",
        });
      })
    }
    getLimitItems();
    getParcels();
    getLimitDeliveries();
    getUsers();

    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [token]);




  return (
    <div className="w-screen max-h-[calc(100vh-3rem)] overflow-hidden flex [&>*:nth-child(2)]:px-4">
      {token &&
        <>
          <Sidebar />
          <Outlet context={{deliveriesTotalDelayed, items, setItems, itemsTotalCount, itemsTotalPrice, parcels, setParcels, deliveries, setDeliveries, users, setUsers, globalError } satisfies DashboardContextType} />
        </>
      }
    </div>
  );
}

