import { useEffect, useState } from "react";
import { api } from "../../api/apiClient";
import { toast } from "react-toastify";
import Item from "../../models/item";
import { useAuth } from "../../context/AuthContext";
import Sidebar from "./Sidebar";
import { Outlet } from "react-router-dom";
import Delivery from "../../models/delivery";
import { User } from "../../models/user";
import { AxiosError } from "axios";

export type DashboardContextType = { items: Item[], deliveries: Delivery[], users: User[], setUsers: (prev: User[]) => void };

export function Dashboard() {

  const [items, setItems] = useState([] as Item[]);
  const [deliveries, setDeliveries] = useState([] as Delivery[]);
  const [users, setUsers] = useState([] as User[]);

  const { token } = useAuth();

  useEffect(() => {
    if (!token) return;
    const getLimitItems = () => {
      api.getItems()
        .then((res) => {
          res.length = 10;
          setItems(res);
        })
        .catch((err: Error) => {
          toast(err.message, {
            type: 'error'
          })
        });
    }
    const getLimitDeliveries = () => {
      api.getDeliveries()
        .then(res => {
          setDeliveries(res);
        })
        .catch((err: Error) => {
          toast(err.message, {
            type: 'error'
          })
        });
    }
    const getUsers = () => {
      api.getUsers().then(u => {
        setUsers(u);
      }).catch((err: Error) => {
        toast(err.message, {
          type: "error",
        });
      })
    }
    getLimitItems();
    getLimitDeliveries();
    getUsers();

    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [token]);




  return (
    <div className="w-screen max-h-[calc(100vh-3rem)] overflow-hidden flex [&>*:nth-child(2)]:px-4">
      {token &&
        <>
          <Sidebar />
          <Outlet context={{ items, deliveries, users, setUsers } satisfies DashboardContextType} />
        </>
      }
    </div>
  );
}

