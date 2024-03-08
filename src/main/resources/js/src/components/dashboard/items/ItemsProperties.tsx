import SingularProperty from "./SingularProperty";

export default function ItemsProperties(props: {properties: string[], widths: string[]}) {
    return (
        <div className="[&>*]:font-semibold">
            {props.properties.map((v, i) => SingularProperty({value: v, width: props.widths[i] }))}
        </div>
    )
}