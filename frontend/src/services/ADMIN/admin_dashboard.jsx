import React from 'react';
import Sidebar from './sidebar';
import Navbar from './Navbar';
import Container from './container';

const Dashboard = () => {
  return (
   <div className='flex'>
       <Sidebar />
       <Navbar />
       <Container/>
   </div>
  )
}

export default Dashboard;