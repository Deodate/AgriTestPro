import React from "react";

const Container = ({ children }) => {
   return (
       <div className="container mx-auto px-6 py-4 ml-64">
           <h1 className="text-2xl font-bold mb-6 text-black">This Admin Panel</h1>
           {children}
       </div>
   )
}

export default Container;