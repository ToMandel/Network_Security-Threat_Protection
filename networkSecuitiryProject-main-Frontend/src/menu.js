
import {
  BrowserRouter as Router,
  Route,
  Routes
} from "react-router-dom";

import OpeningScreen from './pages/openingScreen';
import LoginPage from './pages/loginPage';
import RegisterPage from "./pages/registerPage"
import ChangePasswordPage from "./pages/changePasswordPage"
import SystemScreen from "./pages/systemScreen"


function NewMenu() {
    return (
      <Router>
            <Routes>
            <Route path="*" element={"nothing to see"} />
              <Route index element={<OpeningScreen />} />
              <Route path="RegisterPage" element={<RegisterPage />} />
              <Route path="LoginPage" element={<LoginPage />} />
              <Route path="ChangePasswordPage" element={<ChangePasswordPage />} />
              <Route path="SystemScreen" element={<SystemScreen />} />
            </Routes>
      </Router>
    );
  }
  export default NewMenu;