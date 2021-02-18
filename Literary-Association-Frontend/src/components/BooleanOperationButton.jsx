import React from 'react'
import { ButtonGroup, ToggleButton } from 'react-bootstrap'

const BooleanOperationButton = ({ name, value, setValue }) => {
    const radios = [
        { name: '&', value: 'AND' },
        { name: '|', value: 'OR' }
    ]

    return (
        <ButtonGroup toggle>
            {
                radios.map(r => (
                    <ToggleButton
                        size="sm"
                        key={r.name}
                        value={r.value}
                        type="radio"
                        variant="secondary"
                        name="radio"
                        checked={value === r.value}
                        onChange={({ currentTarget }) => setValue(name, currentTarget.value)}
                    >
                        {r.name}
                    </ToggleButton>
                ))
            }
        </ButtonGroup>
    )
}

export default BooleanOperationButton