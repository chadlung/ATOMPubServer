/*
 * Original code created by the Apache Abdera team
 * http://abdera.apache.org/
 */
package com.giantflyingsaucer.atompubserver;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.abdera.Abdera;
import org.apache.abdera.factory.Factory;
import org.apache.abdera.i18n.iri.IRI;
import org.apache.abdera.model.Content;
import org.apache.abdera.model.Person;
import org.apache.abdera.protocol.server.RequestContext;
import org.apache.abdera.protocol.server.context.ResponseContextException;
import org.apache.abdera.protocol.server.impl.AbstractEntityCollectionAdapter;


public class EmployeeCollectionAdapter extends AbstractEntityCollectionAdapter<Employee> {
    private static final String ID_PREFIX = "tag:giantflyingsaucer.com,2011:employee:entry:";

    private AtomicInteger nextId = new AtomicInteger(1000);
    private Map<Integer, Employee> employees = new HashMap<Integer, Employee>();
    private Factory factory = new Abdera().getFactory();

    @Override
    public String getId(RequestContext request) {
        return "tag:acme.com,2007:employee:feed";
    }

    @Override
    public String getTitle(RequestContext request) {
        return "Acme Employee Database";
    }

    @Override
    public String getAuthor(RequestContext request) {
        return "Acme Industries";
    }

    @Override
    public Iterable<Employee> getEntries(RequestContext request) {
        return employees.values();
    }

    @Override
    public Employee getEntry(String resourceName, RequestContext request) throws ResponseContextException {
        Integer id = getIdFromResourceName(resourceName);
        return employees.get(id);
    }

    private Integer getIdFromResourceName(String resourceName) throws ResponseContextException {
        int idx = resourceName.indexOf("-");
        if (idx == -1) {
            throw new ResponseContextException(404);
        }
        return new Integer(resourceName.substring(0, idx));
    }

    @Override
    public String getName(Employee entry) {
        return entry.getId() + "-" + entry.getName().replaceAll(" ", "_");
    }

    @Override
    public String getId(Employee entry) {
        return ID_PREFIX + entry.getId();
    }

    @Override
    public String getTitle(Employee entry) {
        return entry.getName();
    }

    @Override
    public Date getUpdated(Employee entry) {
        return entry.getUpdated();
    }

    @Override
    public List<Person> getAuthors(Employee entry, RequestContext request) throws ResponseContextException {
        Person author = request.getAbdera().getFactory().newAuthor();
        author.setName("Acme Industries");
        return Arrays.asList(author);
    }

    @Override
    public Object getContent(Employee entry, RequestContext request) {
        Content content = factory.newContent(Content.Type.TEXT);
        content.setText(entry.getName());
        return content;
    }

    @Override
    public Employee postEntry(String title,
                              IRI id,
                              String summary,
                              Date updated,
                              List<Person> authors,
                              Content content,
                              RequestContext request) throws ResponseContextException {
        Employee employee = new Employee();
        employee.setName(content.getText().trim());
        employee.setId(nextId.getAndIncrement());
        employees.put(employee.getId(), employee);

        return employee;
    }

    @Override
    public void putEntry(Employee employee,
                         String title,
                         Date updated,
                         List<Person> authors,
                         String summary,
                         Content content,
                         RequestContext request) throws ResponseContextException {
        employee.setName(content.getText().trim());
    }

    @Override
    public void deleteEntry(String resourceName, RequestContext request) throws ResponseContextException {
        Integer id = getIdFromResourceName(resourceName);
        employees.remove(id);
    }
}
