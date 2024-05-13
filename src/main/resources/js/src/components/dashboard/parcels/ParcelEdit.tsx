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
import Parcel from "../../../models/parcel";

export default function ParcelEdit() {
    const { parcels, globalError } = useOutletContext<DashboardContextType>();
    const { id } = useParams();
    const navigate = useNavigate();
    const parcelValues = parcels.find(u => u.id == parseInt(id ?? ''))
    const [parcel, setParcel] = useState({
        'name': parcelValues?.name,
        'description': parcelValues?.weight
    } as Partial<Parcel>)
    const [submitting, setSubmitting] = useState(false);
    const [deleting, setDeleting] = useState(false);
    const [validationErrors, setValidationErrors] = useState([] as SubError[]);

    useEffect(() => {
        const existingParcel = parcels.find(u => u.id == parseInt(id ?? ''))
        setParcel({
            ...parcel, ...existingParcel
        })
    }, [parcels])


    const handleChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        let { name, value }: {name: any, value: any} = event.target;
        setParcel((data: any) => ({
            ...data, [name]: value
        }))
    }

    const handleSubmit = (event: React.FormEvent<HTMLFormElement>) => {
        event.preventDefault();
        setSubmitting(true);
        if(!parcel.id) {
            setSubmitting(false);
            return;
        }
        const parcelBody: Partial<Parcel> = {
            name: parcel.name,
            weight: parcel.weight as any === '' ? 0 : parcel.weight,
        };

        api.patchParcel(parcelBody, parcel.id).then((_response) => {
            toast('Updated successfully', {type: 'success'});
            navigate('../parcels')
        }).catch((err: AxiosError) => {
            if(err.response?.status === 400 && err.message == 'Validation error') {

                const data = err.response.data as ApiError;
                setValidationErrors(data.subErrors!) //if it's a validation error then there is at least one subError
            } else {
                toast('Could not update the parcel: ' + err.message, {type: 'error'});
            }
        }).finally(() => setSubmitting(false))

    }
    const handleDelete: React.MouseEventHandler<HTMLButtonElement> = (_e) => {
        setDeleting(true);
        api.deleteParcel(parcel.id!)
        .catch((err: AxiosError) => {
            if(err.response?.status === 400 && err.message == 'Validation error') {

                const data = err.response.data as ApiError;
                setValidationErrors(data.subErrors!) //if it's a validation error then there is at least one subError
            } else {
                toast('Could not update the parcel: ' + err.message, {type: 'error'});
            }
        })
        .finally(() => setDeleting(false))
    }

    return (
        <div className="w-[30vw] h-[42vw] border-2 border-gray-300 mx-auto my-auto rounded-md ">
            {globalError.parcels ? <Unauthorized /> : (!parcel.id ?  <Loader /> : 
            <form onSubmit={handleSubmit} className="w-full h-full flex flex-col justify-around">
                <h1 className="text-4xl font-lato w-full text-center">Edit parcel</h1>
                <span>
                    <FormInput errors={validationErrors} name="name" displayName="Name" handleChange={handleChange} style="border-gray-300 h-[3vw]" value={parcel?.name} />
                    <FormInput errors={validationErrors} name="weight" displayName="Weight" handleChange={handleChange} style="border-gray-300 h-[3vw]" value={parcel?.weight?.toString()} />
                </span>
                <div className="flex px-4 justify-between w-full">
                    <button type="submit" className="bg-green-500 text-white font-lato text-xl w-[8vw] rounded-xl grid place-content-center" disabled={submitting}>{submitting ? <Loader small />:'Submit'}</button>
                    <Link to="../parcels" className="bg-gray-500 text-white font-lato text-xl w-[8vw] rounded-xl text-center grid place-content-center">Cancel</Link>
                    <button className="bg-red-500 text-white font-lato text-xl w-[8vw] h-[3vw] rounded-xl text-center grid place-content-center" onClick={handleDelete} disabled={deleting}>{deleting ? <Loader small />:'Delete'}</button>
                </div>
            </form>
            )}
        </div>
    )
}