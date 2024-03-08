import { useEffect, useState } from "react";
import { ApiClient } from "../../api/apiClient";
import { Item } from "../../models/item";
import { toast } from "react-toastify";
import { Items } from "./items/Items";

export function Dashboard() {
  const api = new ApiClient();
  const [items, setItems] = useState([] as Item[]);

  useEffect(() => {
    api
      .getItems()
      .then((res) => {
        setItems(res);
      })
      .catch((err: Error) => {
        toast(err.message, {
          type: "error",
        });
      });

    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  return (
    <>
      <div className="min-h-screen w-screen grid md:grid-cols-3 md:auto-rows-max grid-cols-1 grid-rows-1">
            <Items items={items}/>
            <Items items={items}/>
            <Items items={items}/>
            <Items items={items}/>
      </div>
    </>
  );
}
