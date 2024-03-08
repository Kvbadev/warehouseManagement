export default function SingularProperty(props: {
  value: string | number;
  width: string;
}) {
  return (
    <div
      key={props.value}
      className={`${props.width} px-4 text-gray-500 text-sm font-roboto text-left truncate inline-block box-border`}
    >
      {props.value}
    </div>
  );
}
