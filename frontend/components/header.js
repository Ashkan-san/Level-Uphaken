import Link from 'next/link'
import { signOut } from 'next-auth/client'
import { ToastContainer } from 'react-toastify'

export default function Header({ session }) {
  return (
    <header>
      <nav className="navbar navbar-expand navbar-light ">
        <div className="container-fluid">
          <a href="#" className="burger-btn d-block">
            <i className="bi bi-justify fs-3"></i>
          </a>
          <button className="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
            <span className="navbar-toggler-icon"></span>
          </button>
          {session &&
            <div className="collapse navbar-collapse" id="navbarSupportedContent">
              <ul className="navbar-nav ms-auto mb-2 mb-lg-0">
                <li className="nav-item dropdown me-3">
                  <a className="nav-link dropdown-toggle" href="#" data-bs-toggle="dropdown" aria-expanded="false">
                    <i className='bi bi-bell-fill bi-sub fs-4 text-gray-600'></i>
                  </a>
                  <ul className="dropdown-menu dropdown-menu-end" aria-labelledby="dropdownMenuButton">
                    <li>
                      <h6 className="dropdown-header">Benachrichtigungen</h6>
                    </li>
                    <li><a className="dropdown-item">Benachrichtigung #1</a></li>
                    <li><a className="dropdown-item">Benachrichtigung #2</a></li>
                    <li><a className="dropdown-item">Benachrichtigung #3</a></li>
                    <li><a className="dropdown-item">Benachrichtigung #4</a></li>
                    <li><a className="dropdown-item">Benachrichtigung #5</a></li>
                    <li><a className="dropdown-item">Benachrichtigung #6</a></li>
                  </ul>
                </li>
              </ul>
              <Link href="/user/" >
                <a className="nav-link">
                  <i className='bi bi-person-fill bi-sub fs-4 text-gray-600'></i>
                </a>
              </Link>
              <div className="nav-link dropdown">
                <a className="nav-link dropdown-toggle" href="#" data-bs-toggle="dropdown" aria-expanded="false">
                  <i className='bi bi-gear-fill bi-sub fs-4 text-gray-600'></i>
                </a>
                <ul className="dropdown-menu dropdown-menu-end" aria-labelledby="dropdownMenuButton">
                  <li>
                    <h6 className="dropdown-header">Hallo, {session.user.name}!</h6>
                  </li>
                  <li>
                    <Link href="/quest/" >
                      <a className="dropdown-item">
                      <i className="icon-mid bi bi-shield me-2"></i> Quests
                      </a>
                    </Link>
                  </li>
                  <li>
                    <Link href="/user/settings" >
                      <a className="dropdown-item">
                        <i className="icon-mid bi bi-gear me-2"></i> Einstellungen
                      </a>
                    </Link>
                  </li>
                  <li>
                    <hr className="dropdown-divider" />
                  </li>
                  <li><a className="dropdown-item" href="#" onClick={() => signOut({ callbackUrl: `${window.location.origin}/` })}><i className="icon-mid bi bi-box-arrow-left me-2"></i> Abmelden</a></li>
                </ul>
              </div>
            </div>
          }
        </div>
      </nav>
      <ToastContainer
        position="top-right"
        autoClose={5000}
        hideProgressBar={false}
        newestOnTop={true}
        closeOnClick
        pauseOnFocusLoss
        draggable={false}
        pauseOnHover
      />
    </header>
  );
}