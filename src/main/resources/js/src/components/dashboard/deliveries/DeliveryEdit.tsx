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
import Delivery from "../../../models/delivery";

export default function DeliveryEdit() {
    const { deliveries, globalError } = useOutletContext<DashboardContextType>();
    const { id } = useParams();
    const navigate = useNavigate();
    const parcelValues = deliveries.find(u => u.id == parseInt(id ?? ''))
    const [delivery, setDelivery] = useState({
        'arrivalDate': parcelValues?.arrivalDate,
        'hasArrived': parcelValues?.hasArrived
    } as Partial<Delivery>)
    const [submitting, setSubmitting] = useState(false);
    const [deleting, setDeleting] = useState(false);
    const [validationErrors, setValidationErrors] = useState([] as SubError[]);

    useEffect(() => {
        const existingDelivery = deliveries.find(u => u.id == parseInt(id ?? ''))
        setDelivery({
            ...delivery, ...existingDelivery
        })
    }, [deliveries])


    const handleChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        let { name, value }: {name: any, value: any} = event.target;
        setDelivery((data: any) => ({
            ...data, [name]: value
        }))
    }

    const handleSubmit = (event: React.FormEvent<HTMLFormElement>) => {
        event.preventDefault();
        setSubmitting(true);
        if(!delivery.id) {
            setSubmitting(false);
            return;
        }
        const deliveryBody: Partial<Delivery> = {
            arrivalDate: delivery.arrivalDate,
            hasArrived: delivery.hasArrived,
        };

        api.patchDelivery(deliveryBody, delivery.id).then((_response) => {
            toast('Updated successfully', {type: 'success'});
            navigate('../deliveries')
        }).catch((err: AxiosError) => {
            if(err.response?.status === 400 && err.message == 'Validation error') {

                const data = err.response.data as ApiError;
                setValidationErrors(data.subErrors!) //if it's a validation error then there is at least one subError
            } else {
                toast('Could not update the delivery: ' + err.message, {type: 'error'});
            }
        }).finally(() => setSubmitting(false))

    }
    const handleDelete: React.MouseEventHandler<HTMLButtonElement> = (_e) => {
        setDeleting(true);
        api.deleteDelivery(delivery.id!)
        .catch((err: AxiosError) => {
            if(err.response?.status === 400 && err.message == 'Validation error') {

                const data = err.response.data as ApiError;
                setValidationErrors(data.subErrors!) //if it's a validation error then there is at least one subError
            } else {
                toast('Could not update the delivery: ' + err.message, {type: 'error'});
            }
        })
        .finally(() => setDeleting(false))
    }

    return (
        <div className="w-[30vw] h-[42vw] border-2 border-gray-300 mx-auto my-auto rounded-md ">
            {globalError.deliveries ? <Unauthorized /> : (!delivery.id ?  <Loader /> : 
            <form onSubmit={handleSubmit} className="w-full h-full flex flex-col justify-around">
                <h1 className="text-4xl font-lato w-full text-center">Edit delivery</h1>
                <span>
                    <FormInput errors={validationErrors} name="arrivalDate" displayName="Arrival Date" handleChange={handleChange} style="border-gray-300 h-[3vw]" value={delivery?.arrivalDate} />
                    <FormInput errors={validationErrors} name="hasArrived" displayName="Arrived" handleChange={handleChange} style="border-gray-300 h-[3vw]" value={delivery?.hasArrived+''} />
                </span>
                <div className="flex px-4 justify-between w-full">
                    <button type="submit" className="bg-green-500 text-white font-lato text-xl w-[8vw] rounded-xl grid place-content-center" disabled={submitting}>{submitting ? <Loader small />:'Submit'}</button>
                    <Link to="../deliveries" className="bg-gray-500 text-white font-lato text-xl w-[8vw] rounded-xl text-center grid place-content-center">Cancel</Link>
                    <button className="bg-red-500 text-white font-lato text-xl w-[8vw] h-[3vw] rounded-xl text-center grid place-content-center" onClick={handleDelete} disabled={deleting}>{deleting ? <Loader small />:'Delete'}</button>
                </div>
            </form>
            )}
        </div>
    )
}