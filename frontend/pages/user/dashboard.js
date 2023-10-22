import Head from 'next/head'
import Link from 'next/link'
import Card from '../../components/card'
import { getSession } from 'next-auth/client'
import { requestAPI } from '../../utils/request'

export default function Dashboard({ profile, quests }) {
  const tasks = profile.subjects.flatMap(subject => subject.memos.flatMap(memo => memo.tasks.map(task => ({ subjectId: subject.id, subjectName: subject.subjectName, memoName: memo.title, memoId: memo.id, ...task }))))
  const openTasks = tasks.filter(task => !profile.finishedTasks.some(elm => elm.id === task.id))

  const questsSummary = quests.map(quest => {
    const { finishedLevelSystems, ...remainingQuest } = quest
    return {
      ...remainingQuest,
      done: profile.levelsystem && profile.levelsystem.finishedQuests.some(elm => elm.id === quest.id),
    }
  })

  return (
    <>
      <Head>
        <title>Dashboard</title>
      </Head>
      <div className="page-heading">
        <div className="page-title">
          <div className="row">
            <div className="col mb-3">
              <h3>Dashboard</h3>
            </div>
          </div>
        </div>
        <section className="section row">
          <Card className="offset-1 col-4" title="Aufgaben">
            {(openTasks.length > 0 && (
              <div className="list-group" role="tablist">
                {openTasks.map(task =>
                  <Link key={task.id} href={`/memo/${task.memoId}`}>
                    <a className="list-group-item list-group-item-action" role="tab">
                      {task.subjectName} - {task.memoName} - {task.title}
                    </a>
                  </Link>
                )}
              </div>
            )) || (
                <div className="text-center">
                  Alles erledigt 🥳
                </div>
              )}
          </Card>
          <Card className="offset-2 col-4" title="Quests">
            {(questsSummary.length > 0 && (
              <ul className="widget-todo-list-wrapper ps-0" id="widget-todo-list">
                {questsSummary.map(quest =>
                  <div key={quest.id} className="widget-todo-title-wrapper d-flex justify-content-between align-items-center m-4">
                    <div className="widget-todo-title-area d-flex align-items-center">
                      {(quest.done &&
                        <div className="badge bg-success mx-3" style={{ minWidth: '70px' }}>Erledigt</div>
                      ) || (
                          <div className="badge bg-danger mx-3" style={{ minWidth: '70px' }}>Offen</div>
                        )}
                      <span className="widget-todo-title ms-1"><b>{quest.title}</b></span>
                    </div>
                    <div className="widget-todo-item-action d-flex align-items-center">
                      <div className="badge bg-warning mx-3" style={{ minWidth: '70px' }}>{quest.rewardXP} XP</div>
                    </div>
                  </div>
                )}
              </ul>
            )) || (
                <div className="text-center">
                  Keine Quests vorhanden 😭
                </div>
              )}
          </Card>
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

  try {
    const profile = await requestAPI(`${process.env.API_ENDPOINT}/users/info`, session)
    const quests = await requestAPI(`${process.env.API_ENDPOINT}/quests`, session)
    return {
      props: { session, profile, quests }
    }
  } catch (error) {
    return {
      redirect: {
        destination: '/auth/logout',
        permanent: false,
      }
    }
  }
}