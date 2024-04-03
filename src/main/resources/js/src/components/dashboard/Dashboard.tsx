import { useContext, useEffect, useState } from "react";
import { ApiClient } from "../../api/apiClient";
import { toast } from "react-toastify";
import Item from "../../models/item";
import { useAuth } from "../../context/AuthContext";
import Home from "./Home";
import Sidebar from "./Sidebar";
import { Outlet, useLocation, useNavigate } from "react-router-dom";
import Delivery from "../../models/delivery";

export type DashboardContextType = { items: Item[], deliveries: Delivery[] };

export function Dashboard() {
  const api = new ApiClient();
  const [items, setItems] = useState([] as Item[]);
  const [deliveries, setDeliveries] = useState([] as Delivery[]);
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
            type: "error",
          });
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
    getLimitItems();
    getLimitDeliveries();

    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [token]);




  return (
    <div className="w-screen max-h-[calc(100vh-3rem)] overflow-hidden flex gap-4">
      {token &&
        <>
          <Sidebar />
          <Outlet context={{ items, deliveries } satisfies DashboardContextType} />
        </>
      }
    </div>
  );
}

