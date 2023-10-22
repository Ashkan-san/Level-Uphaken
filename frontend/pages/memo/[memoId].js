import Head from 'next/head'
import getConfig from 'next/config'
import swal from 'sweetalert'
import { getSession } from 'next-auth/client'
import { useEffect, useState, useRef } from 'react'
import { requestAPI } from '../../utils/request'
import { toast } from 'react-toastify'

export default function Memo({ memo, session, profile, quests }) {

  const { publicRuntimeConfig } = getConfig()
  const { subject } = memo

  const [tasks, setTasks] = useState(memo.tasks)
  const [finishedTasks, setFinishedTasks] = useState(profile.finishedTasks)
  const progress = (finishedTasks.filter(task => tasks.some(elm => elm.id === task.id)).length / tasks.length) * 100

  const memoCompleteQuest = quests.find(elm => elm.content === 'MEMO_ABSCHLIESSEN')
  const [memoComplete, setMemoComplete] = useState(profile.levelsystem && profile.levelsystem.finishedQuests.some(elm => elm.id === memoCompleteQuest.id))
  const isMounted = useRef(false);
  useEffect(async () => {
    if (isMounted.current && progress === 100 && !memoComplete) {
      try {
        const updatedLevelsystem = await requestAPI(`${publicRuntimeConfig.API_ENDPOINT}/quests/complete/${memoCompleteQuest.id}`, session, { method: 'PUT' })
        toast.success('🏆 Quest abgeschlossen! +150 XP');
        if (updatedLevelsystem.level !== profile.levelsystem.level) {
          toast.success(`🔥 Neues Level erreicht: ${updatedLevelsystem.levelName}! STONKS!`);         
        }
      } catch (error) {
        console.error(error);
      }
      setMemoComplete(true)
    }
  }, [progress])
  useEffect(() => { isMounted.current = true }, [])

  useEffect(() => {
    setTasks(memo.tasks)
    setFinishedTasks(profile.finishedTasks)
  }, [memo, profile]);

  function Task({ task, isChecked, setFinishedTasks }) {

    async function handleChange(e) {
      e.preventDefault();
      try {
        const updatedUser = await requestAPI(`${publicRuntimeConfig.API_ENDPOINT}/tasks/${isChecked ? 'uncheck' : 'check'}/${task.id}`, session, { method: 'PUT' })
        setFinishedTasks(updatedUser.finishedTasks)

      } catch (error) {
        console.error(error)
      }
    }

    function handleClick() {
      swal({
        title: task.title,
        text: task.content,
        icon: 'info',
      })
    }

    return (
      <li key={task.id} className="widget-todo-item">
        <div className="widget-todo-title-wrapper d-flex justify-content-between align-items-center m-2">
          <div className="widget-todo-title-area d-flex align-items-center">
            {profile.role !== 'PROFESSOR' && (
              <div className="checkbox checkbox-shadow">
                <input type="checkbox" className="form-check-input" id={`checkbox${task.id}`} checked={isChecked} onChange={handleChange} />
                <label htmlFor={`checkbox${task.id}`}></label>
              </div>
            )}
            <span className="widget-todo-title ms-1"><b className={isChecked ? 'text-decoration-line-through' : ''}>{task.title}</b></span>
            <span className="widget-todo-title ms-3" onClick={handleClick} >{task.content.length > 30 ? `${task.content.substring(0, 30)}...` : task.content} <i className="bi bi-info-circle"></i></span>
          </div>
          <div className="widget-todo-item-action d-flex align-items-center">
            {(task.type === 'MANDATORY' && (
              <div className="badge bg-danger me-3" style={{ minWidth: '100px' }}>Pflicht</div>
            )) || (
                <div className="badge bg-info me-3" style={{ minWidth: '100px' }}>Optional</div>
              )}
            <div className="badge bg-warning mx-3" style={{ minWidth: '70px' }}>{task.rewardXP} XP</div>
          </div>
        </div>
      </li>
    )
  }

  function TaskCreator({ memo, setTasks }) {

    async function handleAddTask(e) {
      e.preventDefault();

      try {
        await requestAPI(`${publicRuntimeConfig.API_ENDPOINT}/tasks/`, session, {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json'
          },
          body: JSON.stringify({
            memo: {
              id: memo.id,
            },
            taskId: e.target.taskId.value,
            title: e.target.title.value,
            content: e.target.content.value,
            type: e.target.taskType.value,
            rewardXP: parseInt(e.target.rewardXP.value),
          })
        })
        const updatedMemos = await requestAPI(`${publicRuntimeConfig.API_ENDPOINT}/memos/${memo.id}`, session)
        setTasks(updatedMemos.tasks)
      } catch (error) {
        console.error(error)
        swal({
          title: 'Fehlerhafte Eingabe!',
          icon: 'error',
        })
      }
    }

    return (
      <div className="card widget-todo">
        <div className="card-header border-bottom">
          <h4 className="card-title">Neue Aufgabe anlegen</h4>
        </div>
        <div className="card-body p-4">
          <form onSubmit={handleAddTask}>
            <div className="row">
              <div className="col-12 mb-2">
                <label htmlFor="taskId">Task ID</label>
                <input type="text" className='form-control' id="taskId" placeholder="Task ID" required />
              </div>
              <div className="col-12 mb-2">
                <label htmlFor="title">Title</label>
                <input type="text" className='form-control' id="title" placeholder="Title" required />
              </div>
              <div className="col-12 mb-2">
                <label htmlFor="content">Beschreibung</label>
                <input type="text" className='form-control' id="content" placeholder="Beschreibung" required />
              </div>
              <div className="col-12 mb-2">
                <label htmlFor="taskType">Task Typ</label>
                <select className="form-select" id="taskType">
                  <option value="MANDATORY">Pflicht</option>
                  <option value="OPTIONAL">Optional</option>
                </select>
              </div>
              <div className="col-12 mb-2">
                <label htmlFor="rewardXP">XP</label>
                <input type="number" className='form-control' id="rewardXP" defaultValue="50" min="0" max="200" required />
              </div>
              <div className="col-6 offset-3 mb-2">
                <button className="btn btn-primary btn-block btn-lg shadow-lg mt-5">Hinzufügen</button>
              </div>
            </div>
          </form>
        </div>
      </div>
    )
  }

  return (
    <>
      <Head>
        <title>{subject.subjectName} - {memo.title}</title>
      </Head>

      <div className="page-heading">
        <div className="page-title text-center">
          <h3>{subject.subjectName} - {memo.title}</h3>
        </div>
      </div>

      <div className="page-content">
        <section className="section row m-3">
          <div className="col">
            <div className="card widget-todo">
              <div className="card-header border-bottom d-flex justify-content-between align-items-center">
                <h4 className="card-title d-flex">ToDo</h4>
                <ul className="list-inline d-flex mb-0">
                  <li className="d-flex align-items-center"><i className="bx bx-check-circle font-medium-3 me-50"></i>
                    {/*DROPDOWN 1*/}
                    <div className="dropdown">
                      <div className="dropdown-toggle me-1" role="button" id="dropdownMenuButton"
                        data-bs-toggle="dropdown" aria-haspopup="true"
                        aria-expanded="false"><i className="bi bi-arrow-down-up dropdown-icon"></i>
                      </div>

                      <div className="dropdown-menu" aria-labelledby="dropdownMenuButton">
                        <a className="dropdown-item" href="#">Neuste</a>
                        <a className="dropdown-item" href="#">Älteste</a>
                        <a className="dropdown-item" href="#">Gechecked</a>
                        <a className="dropdown-item" href="#">Unchecked</a>
                      </div>

                    </div>
                  </li>
                  <li className="d-flex align-items-center"><i className="bx bx-sort me-50 font-medium-3"></i>
                    {/*DROPDOWN 2*/}
                    <div className="dropdown">
                      <div className="dropdown-toggle" role="button" id="dropdownMenuButton2"
                        data-bs-toggle="dropdown" aria-haspopup="true"
                        aria-expanded="false">
                        <i className="bi bi-three-dots-vertical dropdown-icon"></i>
                      </div>

                      <div className="dropdown-menu" aria-labelledby="dropdownMenuButton2">
                        <a className="dropdown-item" href="#">Option 1</a>
                        <a className="dropdown-item" href="#">Option 2</a>
                        <a className="dropdown-item" href="#">Option 3</a>
                      </div>
                    </div>
                  </li>
                </ul>
              </div>
              <div className="card-body px-0 py-1">
                <ul className="widget-todo-list-wrapper" id="widget-todo-list">
                  {tasks.map(task => <Task key={task.id} task={task} isChecked={finishedTasks.some(elm => elm.id === task.id)} setFinishedTasks={setFinishedTasks} />)}
                </ul>
              </div>
            </div>

            {profile.role !== 'PROFESSOR' && (
              <div className="card widget-todo">
                <div className="card-body mt-5 p-3">
                  <div className="progress progress-primary mb-4" style={{ width: '80%' }}>
                    <div className="progress-bar progress-label" role="progressbar" style={{ width: `${progress}%` }} aria-valuenow={parseFloat(progress).toFixed(2)} aria-valuemin="0" aria-valuemax="100"></div>
                  </div>
                </div>
              </div>
            )}

            {profile.role === 'PROFESSOR' && <TaskCreator memo={memo} setTasks={setTasks} />}

          </div>

          <div className="col">
            <div className="card">
              <div className="card-header border-bottom mb-3">
                <h4 className="card-title">Beschreibung</h4>
              </div>
              <div className="card-body" dangerouslySetInnerHTML={{ __html: memo.description}}></div>
            </div>
          </div>
        </section>
      </div>
    </>
  )
}


export async function getServerSideProps(context) {
  const session = await getSession(context)

  if (!session) {
    return {
      redirect: {
        destination: '/auth/login',
        permanent: false,
      }
    }
  }

  const { memoId } = context.query;
  try {
    const memo = await requestAPI(`${process.env.API_ENDPOINT}/memos/${memoId}`, session)

    // Check if User is enrolled
    if (memo.subject.prof.emailAddress !== session.user.email && !memo.subject.users.some(user => user.emailAddress === session.user.email)) {
      return {
        redirect: {
          destination: '/memo/notEnrolled',
          permanent: false,
        }
      }
    }

    const quests = await requestAPI(`${process.env.API_ENDPOINT}/quests/`, session)

    return {
      props: { session, memo, quests }
    }

  } catch (error) {
    console.error(error)
    if (error.status && error.status === 403) {
      return {
        redirect: {
          destination: '/auth/logout',
          permanent: false,
        }
      }
    }

    return {
      redirect: {
        destination: '/memo/notFound',
        permanent: false,
      }
    }
  }
}