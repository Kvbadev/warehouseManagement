import { Link, useNavigate, useOutletContext, useParams } from "react-router-dom"
import { DashboardContextType } from "../Dashboard";
import FormInput from "../../signIn/FormInput";
import { useEffect, useState } from "react";
import Loader from "../../utility/Loader";
import { api } from "../../../api/apiClient";
import { toast } from "react-toastify";
import { AxiosError } from "axios";
import ApiError, {SubError} from "../../../models/apiError";
import Unauthorized from "../../utility/Unauthorized";
import Item from "../../../models/item";

export default function ItemEdit() {
    const { items, globalError } = useOutletContext<DashboardContextType>();
    const { id } = useParams();
    const navigate = useNavigate();
    const itemValues = items.find(u => u.id == parseInt(id ?? ''))
    const [item, setItem] = useState({
        'name': itemValues?.name,
        'description': itemValues?.description,
        'quantity': itemValues?.quantity,
        'netPrice': itemValues?.netPrice
    } as Partial<Item>)
    const [submitting, setSubmitting] = useState(false);
    const [deleting, setDeleting] = useState(false);
    const [validationErrors, setValidationErrors] = useState([] as SubError[]);

    useEffect(() => {
        const existingItem = items.find(u => u.id == parseInt(id ?? ''))
        setItem({
            ...item, ...existingItem
        })
    }, [items])


    const handleChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        let { name, value }: {name: any, value: any} = event.target;
        setItem((data: any) => ({
            ...data, [name]: value
        }))
    }

    const handleSubmit = (event: React.FormEvent<HTMLFormElement>) => {
        event.preventDefault();
        setSubmitting(true);
        if(!item.id) {
            setSubmitting(false);
            return;
        }
        const itemBody: Partial<Item> = {
            name: item.name,
            description: item.description,
            quantity: item.quantity,
            netPrice: (item.netPrice ?? 0) * 100,
        };

        api.patchItem(itemBody, item.id).then((_response) => {
            toast('Updated successfully', {type: 'success'});
            navigate('../items')
        }).catch((err: AxiosError) => {
            if(err.response?.status === 400 && err.message == 'Validation error') {

                const data = err.response.data as ApiError;
                setValidationErrors(data.subErrors!) //if it's a validation error then there is at least one subError
            } else {
                toast('Could not update the item: ' + err.message, {type: 'error'});
            }
        }).finally(() => setSubmitting(false))

    }
    const handleDelete: React.MouseEventHandler<HTMLButtonElement> = (_e) => {
        api.deleteItem(item.id!)
        .catch((err: AxiosError) => {
            if(err.response?.status === 400 && err.message == 'Validation error') {

                const data = err.response.data as ApiError;
                setValidationErrors(data.subErrors!) //if it's a validation error then there is at least one subError
            } else {
                toast('Could not update the item: ' + err.message, {type: 'error'});
            }
        })
        .finally(() => setDeleting(false))
    }

    return (
        <div className="w-[30vw] h-[42vw] border-2 border-gray-300 mx-auto my-auto rounded-md ">
            {globalError.items ? <Unauthorized /> : (!item.id ?  <Loader /> : 
            <form onSubmit={handleSubmit} className="w-full h-full flex flex-col justify-evenly">
                <h1 className="text-4xl font-lato w-full text-center">Edit item</h1>
                <FormInput errors={validationErrors} name="name" displayName="Name" handleChange={handleChange} style="border-gray-300 h-[3vw]" value={item?.name} />
                <FormInput errors={validationErrors} name="description" displayName="Description" handleChange={handleChange} style="border-gray-300 h-[3vw]" value={item?.description} />
                <FormInput errors={validationErrors} name="quantity" displayName="Quantity" handleChange={handleChange} style="border-gray-300 h-[3vw]" value={item?.quantity?.toString()} />
                <FormInput errors={validationErrors} name="netPrice" displayName="Price" handleChange={handleChange} style="border-gray-300 h-[3vw]" value={item?.netPrice?.toString()} />
                <div className="flex px-4 justify-between w-full">
                    <button type="submit" className="bg-green-500 text-white font-lato text-xl w-[8vw] rounded-xl grid place-content-center" disabled={submitting}>{submitting ? <Loader small />:'Submit'}</button>
                    <Link to="../items" className="bg-gray-500 text-white font-lato text-xl w-[8vw] rounded-xl text-center grid place-content-center">Cancel</Link>
                    <button className="bg-red-500 text-white font-lato text-xl w-[8vw] h-[3vw] rounded-xl text-center grid place-content-center" onClick={handleDelete} disabled={deleting}>{deleting ? <Loader small />:'Delete'}</button>
                </div>
            </form>
            )}
        </div>
    )
}