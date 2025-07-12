import './App.css';
import HomePage from './Pages/HomePage';
import About from './Pages/About';
import Crops from './Pages/Crops';
import Markets from './Pages/Markets';
import ProductDetails from './Pages/ProductDetails/ProductDetails';
import News from './Pages/News';
import LatestNews from './Pages/News/LatestNews';
import PressReleases from './Pages/News/PressReleases';
import Gallery from './Pages/Gallery';
import Weather from './Pages/Weather';
import Policy from './Pages/Policy';
import PolicyDetails from './Pages/PolicyDetails/PolicyDetails';
import CropDetails from './Pages/CropDetails/CropDetails';
import Contact from './Pages/Contacts';
import Products from './Pages/Products';
import ProductsPage from './Pages/ProductsPage';
import SignUp from './Pages/Signup';
import Login from './Pages/Login';
import Dashboard from './Pages/Dashboard';
import ResetPassword from './Pages/ResetPassword';
// Compliance Checklist imports
import ComplianceChecklistList from './Pages/ComplianceChecklist/ComplianceChecklistList';
import ComplianceChecklistCreate from './Pages/ComplianceChecklist/ComplianceChecklistCreate';
import ComplianceChecklistDetail from './Pages/ComplianceChecklist/ComplianceChecklistDetail';

import { Routes, Route, Navigate } from 'react-router-dom';
import ProtectedRoute from './Components/common/ProtectedRoute';

// Toast notifications
import { ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

function App() {
  return (
      <>
        <ToastContainer 
          position="top-right"
          autoClose={5000}
          hideProgressBar={false}
          newestOnTop
          closeOnClick
          rtl={false}
          pauseOnFocusLoss
          draggable
          pauseOnHover
        />
        <Routes>
          {/* Public Routes */}
      <Route path="/login" element={<Login />} />
      <Route path="/signup" element={<SignUp />} />
      <Route path="/reset-password" element={<ResetPassword />} />

          {/* Home */}
          <Route path='/' element={<HomePage />}/>
          
          {/* About */}
          <Route path='/about' element={<About />}/>
          
          <Route path='/markets' element={<Markets />}/>
          <Route path='/markets/products' element={<Products />}/>
          <Route path='/markets/products/catalog' element={<Products />}/>
          
          {/* News */}
          <Route path='/news' element={<News />}/>
          <Route path='/news/latest' element={<LatestNews />}/>
          <Route path='/news/press-releases' element={<PressReleases />}/>
          <Route path='/news/gallery' element={<Gallery />}/>
          
          {/* Weather */}
          <Route path='/weather' element={<Weather />}/>

          <Route path='/contact' element={<Contact />}/>
          
          {/* Crops */}
          <Route path='/crops' element={<Crops />}/>
          <Route path='/crops/:cropId' element={<CropDetails />}/>
          
          {/* Policies */}
          <Route path='/policies' element={<Policy />}/>
          <Route path='/policyDetails/:policyId' element={<PolicyDetails />}/>
          
          {/* Product Details */}
          <Route path='/product/:productId' element={<ProductDetails />}/>

      {/* Protected Routes */}
      <Route path="/dashboard" element={
        <ProtectedRoute>
          <Dashboard />
        </ProtectedRoute>
      } />
      
      {/* Products Management Page with DataTable */}
      <Route path="/products-management" element={
        <ProtectedRoute>
          <ProductsPage />
        </ProtectedRoute>
      } />

      {/* Compliance Checklist Routes */}
      <Route path="/compliance-checklist" element={
        <ProtectedRoute>
          <ComplianceChecklistList />
        </ProtectedRoute>
      } />
      <Route path="/compliance-checklist/create" element={
        <ProtectedRoute>
          <ComplianceChecklistCreate />
        </ProtectedRoute>
      } />
      <Route path="/compliance-checklist/:id" element={
        <ProtectedRoute>
          <ComplianceChecklistDetail />
        </ProtectedRoute>
      } />

      {/* Default redirect to home */}
      <Route path="*" element={<Navigate to="/" replace />} />
        </Routes>
      </>
  );
}

export default App;