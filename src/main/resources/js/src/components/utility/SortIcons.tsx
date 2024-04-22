import { useEffect, useState } from "react"
import { FaSort, FaSortDown, FaSortUp } from "react-icons/fa"
import { SortState } from "../../models/sortState"

export function SortIcons({property, sortedBy, setSortedBy, state, setState}: {property: string, sortedBy: any, setSortedBy: (prev: any) => void, state: SortState, setState: (oldState: SortState) => void}) {

    const handleClick = () => {
        //reset state if previously sorted on another property
        let prevState = state;
        if(sortedBy != property) {
            prevState = 'UNSORTED'
        }
        switch(prevState) {
            case 'UNSORTED':
                setState('ASCENDING')
                break
            case 'ASCENDING':
                setState('DESCENDING')
                break
            default:
                setState('UNSORTED')
                break
        }

        setSortedBy(property)
    }
    return (
        <span onClick={handleClick} className="cursor-pointer">
            {state === 'UNSORTED' || sortedBy != property ?
                <FaSort size={20} className="relative top-[2px] pr-2" /> : 
                (state === 'ASCENDING' ? 
                    <FaSortUp size={20} className="relative top-[2px] pr-2" /> : 
                    <FaSortDown size={20} className="relative top-[2px] pr-2" />)
            }
        </span>
    )
}