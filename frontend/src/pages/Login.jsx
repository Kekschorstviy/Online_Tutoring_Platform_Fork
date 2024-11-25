import NavBar from "../components/Navbar";
import { useState } from "react";
import { useNavigate } from "react-router-dom";

function Login() {
  const navigate = useNavigate();
  const [passwordVisible, setPasswordVisible] = useState(false);
  const toggleVisibility = () => {
    setPasswordVisible(!passwordVisible);
  };

  return (
    <div className="relative min-h-screen bg-white overflow-hidden">
      <NavBar isLoggedIn={false} currentPage={"/"} />
      <div className="absolute top-0 left-0 right-0 bottom-0 flex items-center justify-center w-full px-4 ">
        <div className="flex flex-col justify-center w-full max-w-xl bg-gray-100 p-10 rounded-2xl font-merriweather_sans">
          <div className="flex items-center justify-center text-2xl">
            Welcome back! 👋
          </div>
          <label className="text-sm font-medium mt-7 text-black">Email</label>
          <div className="relative mt-1">
            <span className="material-symbols-rounded absolute inset-y-0 left-3 flex items-center text-gray-800">
              email
            </span>
            <input
              type="email"
              name="email"
              placeholder="username@thu.de"
              className="pl-11 pr-4 py-2 rounded-md w-full border border-gray-300 focus:ring-1 focus:ring-gray-950 focus:outline-none"
            />
          </div>
          <label className="text-sm font-medium mt-4 text-black">
            Password
          </label>
          <div className="relative mt-1">
            <span className="material-symbols-rounded absolute inset-y-0 left-3 flex items-center text-gray-800">
              lock
            </span>
            <input
              type={passwordVisible ? "text" : "password"}
              name="password"
              placeholder="Enter atleast 8 characters"
              className="pl-11 pr-4 py-2 rounded-md w-full border border-gray-300 focus:ring-1 focus:ring-gray-950 focus:outline-none"
            />
            <span
              className="cursor-pointer material-symbols-rounded absolute inset-y-0 right-3 flex items-center text-gray-800"
              onClick={toggleVisibility}
            >
              {passwordVisible ? "visibility" : "visibility_off"}
            </span>
          </div>
          <div
            className="inline-flex mt-2 justify-end text-blue-900 cursor-pointer self-end w-auto"
            onClick={() => {
              alert("Too bad :(");
            }}
          >
            Forgot password?
          </div>
          <button className="bg-blue-900 text-white py-2 px-1 rounded-md mt-7 hover:bg-blue-800 focus:outline-none">
            Log In
          </button>
          <div
            className="inline-flex items-center justify-center text-sm mt-3 cursor-pointer w-fit mx-auto"
            onClick={() => navigate("/signup")}
          >
            Not a member yet?
            <span className="ml-1">
              <b>Register now</b>
            </span>
          </div>
        </div>
      </div>
    </div>
  );
}

export default Login;