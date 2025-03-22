import React from "react";

const StatisticsContainer = () => {
  // Sample data for recent orders
  const recentOrders = [
    { date: "16 July, 2021", name: "Elvis Presley", shipTo: "Tupelo, MS", paymentMethod: "VISA", cardNumber: "3719", saleAmount: 312.44 },
    { date: "16 July, 2021", name: "Paul McCartney", shipTo: "London, UK", paymentMethod: "VISA", cardNumber: "2574", saleAmount: 866.99 },
    { date: "16 July, 2021", name: "Tom Scholz", shipTo: "Boston, MA", paymentMethod: "MC", cardNumber: "1253", saleAmount: 100.81 },
    { date: "16 July, 2021", name: "Michael Jackson", shipTo: "Gary, IN", paymentMethod: "AMEX", cardNumber: "2000", saleAmount: 654.39 },
    { date: "15 July, 2021", name: "Bruce Springsteen", shipTo: "Long Branch, NJ", paymentMethod: "VISA", cardNumber: "5919", saleAmount: 212.79 }
  ];

  // Sample data for the sales chart
  const hours = ["00:00", "03:00", "06:00", "09:00", "12:00", "15:00", "18:00", "21:00", "24:00"];
  const salesData = [0, 200, 600, 800, 1400, 1800, 2400, 2400, 2400];

  return (
    <div className="w-full space-y-4 pt-7">
      {/* Top row */}
      <div className="flex flex-col md:flex-row gap-4">
        {/* Today's Sales Chart */}
        <div className="bg-white p-4 rounded-lg shadow flex-1">
          <h2 className="text-lg text-gray-500 mb-2">Today</h2>
          <div className="h-40">
            {/* Simple line chart representation */}
            <svg viewBox="0 0 1000 300" className="w-full h-full">
              <polyline
                points={hours.map((hour, index) => `${(index * 1000) / (hours.length - 1)},${300 - (salesData[index] / 2400) * 280}`).join(' ')}
                fill="none"
                stroke="#6b7ee8"
                strokeWidth="2"
              />
              {/* X-axis */}
              <line x1="0" y1="280" x2="1000" y2="280" stroke="#e5e7eb" strokeWidth="1" />
              
              {/* Y-axis labels */}
              <text x="10" y="280" fontSize="12" fill="#9ca3af">0</text>
              <text x="10" y="220" fontSize="12" fill="#9ca3af">600</text>
              <text x="10" y="160" fontSize="12" fill="#9ca3af">1200</text>
              <text x="10" y="100" fontSize="12" fill="#9ca3af">1800</text>
              <text x="10" y="40" fontSize="12" fill="#9ca3af">2400</text>
              
              {/* X-axis labels - only show some for compactness */}
              {hours.filter((_, i) => i % 2 === 0 || i === hours.length - 1).map((hour, index) => {
                const position = hour === "24:00" ? 1000 : (hours.indexOf(hour) * 1000) / (hours.length - 1);
                return (
                  <text 
                    key={hour}
                    x={position} 
                    y="300" 
                    fontSize="12" 
                    fill="#9ca3af"
                    textAnchor="middle"
                  >
                    {hour}
                  </text>
                );
              })}
            </svg>
          </div>
        </div>

        {/* Recent Deposits */}
        <div className="bg-white p-4 rounded-lg shadow md:w-72">
          <h2 className="text-lg text-gray-500 mb-2">Recent Expense</h2>
          <div className="mb-1">
            <div className="text-3xl font-semibold">$3,024.00</div>
            <div className="text-sm text-gray-500">on 30 July, 2021</div>
          </div>
          <div className="mt-3">
            <a href="#" className="text-sm text-blue-500 hover:text-blue-600">Cost Tracking</a>
          </div>
        </div>
      </div>

      {/* Recent Orders */}
      <div className="bg-white p-4 rounded-lg shadow">
        <h2 className="text-lg text-gray-500 mb-2">Recent Orders</h2>
        <div className="overflow-x-auto">
          <table className="min-w-full">
            <thead>
              <tr className="border-b">
                <th className="text-left py-1 px-2 text-sm text-gray-500 font-normal">Date</th>
                <th className="text-left py-1 px-2 text-sm text-gray-500 font-normal">Name</th>
                <th className="text-left py-1 px-2 text-sm text-gray-500 font-normal">Ship To</th>
                <th className="text-left py-1 px-2 text-sm text-gray-500 font-normal">Payment Method</th>
                <th className="text-right py-1 px-2 text-sm text-gray-500 font-normal">Sale Amount</th>
              </tr>
            </thead>
            <tbody>
              {recentOrders.map((order, index) => (
                <tr key={index} className="border-b hover:bg-gray-50">
                  <td className="py-1 px-2 text-sm text-gray-600">{order.date}</td>
                  <td className="py-1 px-2 text-sm text-gray-600">{order.name}</td>
                  <td className="py-1 px-2 text-sm text-gray-600">{order.shipTo}</td>
                  <td className="py-1 px-2 text-sm text-gray-600">{order.paymentMethod} •••• {order.cardNumber}</td>
                  <td className="py-1 px-2 text-sm text-gray-600 text-right">{order.saleAmount.toFixed(2)}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
        <div className="mt-2">
          <a href="#" className="text-sm text-blue-500 hover:text-blue-600">See more orders</a>
        </div>
      </div>
    </div>
  );
};

export default StatisticsContainer;