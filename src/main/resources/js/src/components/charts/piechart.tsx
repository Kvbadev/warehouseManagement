import { render } from '@testing-library/react';
import { PieChart, ResponsiveContainer, Cell, Pie, Tooltip, Legend  } from 'recharts';

const data = [
  { name: 'ROLE_ADMIN', value: 400 },
  { name: 'ROLE_USER', value: 300 },
  { name: 'ROLE_STAFF', value: 200 },
];

const COLORS = ['#0088FE', '#00C49F', '#FFBB28', '#FF8042'];

export default function CustomPieChart({data}: {data?: {name: string, value: number}[]}) {
  if(data == null) {
    data = [
      { name: 'Group A', value: 400 },
      { name: 'Group B', value: 300 },
      { name: 'Group C', value: 300 },
      { name: 'Group D', value: 200 },
    ];

  }
  
  const COLORS = ['#0088FE', '#00C49F', '#FFBB28', '#FF8042'];

  return (
    <ResponsiveContainer width="100%" height="100%">
      <PieChart>
        <Pie
          data={data}
          cx="50%"
          cy="50%"
          labelLine={false}
          outerRadius={120}
          fill="#8884d8"
          dataKey="value"
          nameKey="name"
          label={({name}) => name}
        >
          {data.map((entry, index) => (
            <Cell key={`cell-${index}`} fill={COLORS[index % COLORS.length]} />
          ))}
        </Pie>
        <Tooltip />
        <Legend />
      </PieChart>
    </ResponsiveContainer>
  )
}
