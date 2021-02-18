import React, { useState } from 'react'
import { ButtonGroup, ToggleButton } from 'react-bootstrap'

const BooleanOperationButton = () => {
    const radios = [
        { name: '&', value: 'AND' },
        { name: '|', value: 'OR' }
    ]

    const [radioValue, setRadioValue] = useState(radios[0].value)


    return (
        <ButtonGroup toggle>
            {
                radios.map(r => (
                    <ToggleButton
                        key={r.name}
                        value={r.value}
                        type="radio"
                        variant="secondary"
                        name="radio"
                        checked={radioValue === r.value}
                        onChange={({ currentTarget }) => setRadioValue(currentTarget.value)}
                    >
                        {r.name}
                    </ToggleButton>
                ))
            }
        </ButtonGroup>
    )
}

export default BooleanOperationButton