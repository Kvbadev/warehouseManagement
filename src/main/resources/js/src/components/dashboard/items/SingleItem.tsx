import { Item } from "../../../models/item";
import SingularProperty from "./SingularProperty";

export function SingleItem(item: Item, itemsPropertyWidths: string[]) {
    return (
        <div key={item.id} className="block w-full">
            {Object.values(item).map((val, i) => SingularProperty({ value: val as any, width: itemsPropertyWidths[i] }))}
        </div>
    )
}