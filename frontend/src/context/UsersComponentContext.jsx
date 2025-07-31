import { useEffect, useState, useContext } from "react";
import { AuthContext } from "./AuthContext";
import { useNavigate } from "react-router-dom";
import {activateUser, getUsers} from "../api/users";

export function UsersComponentContext() {
  const authContext = useContext(AuthContext);
  const navigate = useNavigate();

  const [users, setUsers] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    if (!authContext.loading) {
      const user = authContext.user;
      if (!user || user.roleName !== "ADMIN") {
        navigate("/");
      }
    }
  }, [authContext.user, authContext.loading, navigate]);

  useEffect(() => {
    setLoading(true);
    getUsers(authContext.user.sub)
      .then(res => {
        let users = res?.data?.users || [];
        let roleNames = res?.data?.roleNames || [];
        for (let i = 0; i < users.length; i++) {
          users[i].roleName = roleNames[i] !== undefined ? roleNames[i] : 'N/A'
        }
        setUsers(users || []);
        setLoading(false);
      })
      .catch(err => {
        setError(err);
        setLoading(false);
      });
  }, [authContext.user]);

  const handleActivateUser = async (targetUserId, action) => {
    let data = {}
    data["targetUserId"] = targetUserId
    data["action"] = "true"
    try {
      let result = await activateUser(authContext.user.sub, data);
      if (result.data.data != null) {
        setUsers(prev =>
          prev.map(user =>
            user.id === targetUserId ? { ...user, isActive: result.data.data } : user
          )
        );
      }
    } catch (err) {
      console.error("Failed to activate/deactivate user", err);
    }
  };

  return {
    users,
    loading,
    error,
    handleActivateUser,
  };
}