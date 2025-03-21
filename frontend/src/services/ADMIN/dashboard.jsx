import React from "react";
import Sidebar from "./sidebar";
import Navbar from "./Navbar";

const Dashboard = () => {
    return (
        <div className="flex flex-col min-h-screen">
            <Navbar />
            <div className="flex flex-1">
                <Sidebar />
                <div className="flex-1 ml-64 p-6 bg-gray-100">
                    <div className="container mx-auto">
                        <h1 className="text-2xl font-bold mb-6">Dashboard</h1>
                        <div className="bg-white rounded-lg shadow p-6">
                            <p>Welcome to your dashboard</p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    )
}

export default Dashboard;