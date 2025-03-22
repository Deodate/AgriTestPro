import React from "react";
import StatisticsContainer from "./StatisticsContainer";

const Container = ({ children }) => {
    return (
        <div className="w-full px-6 py-4 ml-64" style={{ backgroundColor: "#c5e4fa" }}>
            <StatisticsContainer />
            {children}
        </div>
    )
}

export default Container;