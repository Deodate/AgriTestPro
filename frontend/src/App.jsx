import { Routes, Route } from "react-router-dom";
import HomePage from "./pages/HomePage";
import SignupPage from "./components/auth/SignupPage";
import Dashboard from "./services/ADMIN/admin_dashboard.jsx";
import Sidebar from "./services/ADMIN/sidebar";
import ProtectedRoutes from "./components/auth/ProtectedRoute";


function App() {
  return (
    <Routes>
      <Route path="/" element={<HomePage />} />
      <Route path="/signup" element={<SignupPage />} />
      <Route path="/login" element={<SignupPage />} />
      {/* <Route path="/dashboard/*" element={<Dashboard />} /> */}
      <Route element={<ProtectedRoutes />}>
        <Route path="/dashboard/*" element={<Dashboard />} />
        <Route path="/sidebar" element={<Sidebar />} />
      </Route>
    </Routes>
  );
}

export default App;