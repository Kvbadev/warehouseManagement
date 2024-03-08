import { Item } from "../../../models/item";
import ItemsLoader from "./ItemsLoader";
import ItemsProperties from "./ItemsProperties";
import { SingleItem } from "./SingleItem";

export function Items(props: { items: Item[] }) {
  const { items } = props;
  const itemsPropertyWidths = ["w-1/12", "w-1/4", "w-1/4", "w-1/4", "w-1/6"];
  const itemsProperties = ["Id", "Name", "Description", "Quantity", "Price"];

  return (
        <div className="border-4 border-sky-100 m-2 max-h-80">
        {/* If there are not items, show ItemsLoader */}
        {!items.length ? (
          <ItemsLoader />
        ): (
          <>
          <h1 className="box-border p-4 text-3xl font-roboto block text-center h-1/4">
            Items
          </h1>
          {/* <div className="flex flex-col md:flex-row md:items-center md:justify-start md:min-w-24 md:h-3/4"> */}
          <div className="min-w-24 h-3/4 flex flex-col justify-evenly">
              <ItemsProperties
                properties={itemsProperties}
                widths={itemsPropertyWidths}
              />
              {items.map((item) => SingleItem(item, itemsPropertyWidths))}
          </div>
          </>
      )}
      </div>
  );
}
